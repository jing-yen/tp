package paypals.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paypals.Activity;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class DeleteCommandTest extends PayPalsTest {

    @BeforeEach
    public void setUpDelete() throws PayPalsException {
        AddCommand c1 = new AddCommand("d/lunch n/john f/jane a/30 f/jake a/20");
        c1.execute(activityManager, false);
        AddCommand c2 = new AddCommand("d/dinner n/jane f/john a/20 f/jake a/20");
        c2.execute(activityManager, false);
    }

    @Test
    public void execute_validIdentifier_correctlyUpdatesNetOwedMap() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/2");
        command.execute(activityManager, false);
        HashMap<String, Double> netOwedMap = getNetOwedMap();
        for (String key : netOwedMap.keySet()) {  //ensure set up is correct
            if (key.equals("john")) {
                assertEquals(50.0, netOwedMap.get(key), 0.001, "For john expected 50.0");
            } else if (key.equals("jane")) {
                assertEquals(-30.0, netOwedMap.get(key), 0.001, "For jane expected -30.0");
            } else if (key.equals("jake")) {
                assertEquals(-20.0, netOwedMap.get(key), 0.001, "For jake expected -20.0");
            } else {
                fail("Unexpected key in netOwedMap: " + key);
            }
        }
    }

    private HashMap<String, Double> getNetOwedMap() {
        HashMap<String, Double> netOwedMap = new HashMap<>();
        int activitiesSize = activityManager.getSize();
        for (int i = 0; i < activitiesSize; i++) {
            Activity activity = activityManager.getActivity(i);
            String payerName = activity.getPayer().getName();
            Collection<Person> allFriends = activity.getAllFriends();
            for (Person friend : allFriends) {
                if (friend.hasPaid()){
                    continue;
                }
                String friendName = friend.getName();
                Double amountOwed = friend.getAmount();
                netOwedMap.put(friendName, netOwedMap.getOrDefault(friendName,0.0) - amountOwed);
                netOwedMap.put(payerName, netOwedMap.getOrDefault(payerName,0.0) + amountOwed);
            }
        }
        return netOwedMap;
    }

    @Test
    public void execute_expensePaid_correctlyUpdatesNetOwedMap() throws PayPalsException {
        PaidCommand paidCommand = new PaidCommand("n/john i/1");
        paidCommand.execute(activityManager, false);
        DeleteCommand deleteCommand = new DeleteCommand("i/2");
        deleteCommand.execute(activityManager, false);
        HashMap<String, Double> netOwedMap = getNetOwedMap();
        assertTrue(netOwedMap.isEmpty(), "Expected empty netOwedMap");
    }

    @Test
    public void execute_deleteAllActivities_netOwedMapIsEmpty() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/1");
        command.execute(activityManager, false);
        DeleteCommand command2 = new DeleteCommand("i/1");
        command2.execute(activityManager, false);
        HashMap<String, Double> netOwedMap = getNetOwedMap();
        assertTrue(netOwedMap.isEmpty(), "Expected empty netOwedMap");
    }

    @Test
    public void execute_outOfBoundsIdentifier_exceptionThrown() {
        DeleteCommand command = new DeleteCommand("i/5");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }
    }

    @Test
    public void execute_noIdentifier_exceptionThrown() {
        DeleteCommand command = new DeleteCommand("");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_IDENTIFIER);
        }
    }

    @Test
    public void execute_invalidIdentifier_exceptionThrown() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/999999999999999999999999");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.INVALID_IDENTIFIER);
        }
    }

    @Test
    public void execute_zeroIdentifier_exceptionThrown() {
        DeleteCommand command = new DeleteCommand("i/0");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException for zero index");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }
    }

    @Test
    public void execute_identifierWithSpaces_validCommandStillParses() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("   i/2   ");
        command.execute(activityManager, false);
        assertEquals(1, activityManager.getSize(), "Activity should be deleted successfully despite spaces");
    }

    @Test
    public void execute_identifierWithExtraText_ignoresTrailingGarbage() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/2 extra text");
        command.execute(activityManager, false);
        assertEquals(1, activityManager.getSize(), "Activity should be deleted even with extra trailing text");
    }

    @Test
    public void isExit_someInput_expectFalse() {
        DeleteCommand command = new DeleteCommand("i/2");

        assertFalse(command.isExit(), "isExit() should return false for a DeleteCommand");
    }

    // Test deletion on an empty ActivityManager. Should throw an OUTOFBOUNDS_IDENTIFIER exception.
    @Test
    public void execute_deleteOnEmptyActivityManager_exceptionThrown() {
        // Create a new, empty ActivityManager.
        ActivityManager emptyManager = new ActivityManager();
        DeleteCommand command = new DeleteCommand("i/1");
        try {
            command.execute(emptyManager, false);
            fail("Expected PayPalsException when deleting from an empty ActivityManager");
        } catch (PayPalsException e) {
            // With no activities, any identifier is out of bounds.
            assertEquals(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER.getMessage() + "1", e.getMessage());
        }
    }

    // Test that if multiple identifiers are provided, the first one is used.
    // For instance, if the command contains "i/1 i/2", then the first activity is deleted.
    @Test
    public void execute_multipleIdentifier_firstOneUsed() throws PayPalsException {
        // Issue a delete command with two identifiers.
        DeleteCommand command = new DeleteCommand("i/1 i/2");
        command.execute(activityManager, false);
        assertEquals("dinner", activityManager.getActivity(0).getDescription());
    }

    // Test that an identifier with leading zeros is parsed correctly.
    // For example, "i/02" should be interpreted as deleting the second activity.
    @Test
    public void execute_identifierWithLeadingZeros_validCommand() throws PayPalsException {
        // setUpDelete() already creates 2 activities.
        DeleteCommand command = new DeleteCommand("i/02");
        command.execute(activityManager, false);
        // After deleting the second activity, only one activity remains.
        assertEquals(1, activityManager.getSize(),
                "Expected activity manager size to be 1 after deletion using leading zeros.");
    }

    // Test that extra trailing text after the identifier is ignored.
    // For example, "i/2 random extra text" should still delete the activity with identifier 2.
    @Test
    public void execute_validIdentifierWithExtraParameters_success() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/2 random extra text");
        command.execute(activityManager, false);
        assertEquals(1, activityManager.getSize(), "Activity should be deleted even with extra trailing parameters.");
    }

    // Test that a command with "i/" but no number throws a NO_IDENTIFIER exception.
    @Test
    public void execute_missingNumberAfterIdentifier_exceptionThrown() {
        DeleteCommand command = new DeleteCommand("i/");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException for missing number after i/");
        } catch (PayPalsException e) {
            // Since the regex fails to match any digits, it should throw NO_IDENTIFIER.
            assertEquals(ExceptionMessage.NO_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    // Test that an identifier with a space between the slash and the number
    // (e.g., "i/ 1") does not match and throws NO_IDENTIFIER.
    @Test
    public void execute_identifierWithSpaceAfterSlash_exceptionThrown() {
        DeleteCommand command = new DeleteCommand("i/ 1");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException for identifier with a space after the slash");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    // Test that an identifier with multiple spaces between "i/" and the number (e.g., "i/    1") throws NO_IDENTIFIER.
    @Test
    public void execute_identifierWithMultipleSpaces_exceptionThrown() {
        DeleteCommand command = new DeleteCommand("i/    1");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException for identifier with multiple spaces between i/ and number");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    // Test that a command containing extra leading text before the identifier still deletes the correct activity.
    // For example, "random text i/1" should match the identifier "1".
    @Test
    public void execute_identifierNotAtStart_success() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("random extra text i/1");
        command.execute(activityManager, false);
        // With the first activity deleted, only one activity should remain.
        assertEquals(1, activityManager.getSize());
    }

    // Test that a command with multiple "i/" tokens but with extra text in between uses only the first match.
    // For example, "i/1 some text i/2" should delete the first activity.
    @Test
    public void execute_multipleIdentifierWithExtraText_firstOneUsed() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/1 some extra text i/2");
        command.execute(activityManager, false);
        // After deletion, the remaining activity should be the one that originally had description "d/dinner".
        assertEquals("dinner", activityManager.getActivity(0).getDescription());
    }

    // Test that a command with trailing whitespace after the identifier is still parsed correctly.
    @Test
    public void execute_identifierWithTrailingWhitespace_success() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/2   ");
        command.execute(activityManager, false);
        // After deleting the second activity, only one should remain.
        assertEquals(1, activityManager.getSize());
    }

    @Test
    public void execute_negativeIdentifier_throwsException() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/-3");
        try {
            command.execute(activityManager, false);
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER.getMessage() + "-3", e.getMessage());
        }
    }
}
