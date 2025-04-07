package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.Activity;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AddEqualCommandTest extends PayPalsTest {
    @Test
    public void execute_validMultipleFriends_correctlyUpdatesNetOwedMap() throws PayPalsException {
        String command = "addequal d/Trip n/Eve f/Frank f/Gina a/30";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        addEqualCommand.execute(activityManager, false);

        assertEquals(1, activityManager.getSize());
        Activity activity = activityManager.getActivity(0);
        assertEquals("Trip", activity.getDescription());
        assertEquals("Eve", activity.getPayer().getName());

        Person payer = activity.getPayer();
        assertEquals(-20, payer.getAmount());
        ArrayList<Person> friends = new ArrayList<>(activity.getAllFriends());
        assertEquals(2, friends.size());

        for (Person person : friends) {
            if (person.getName().equals("Frank")) {
                assertEquals(10, person.getAmount());
            } else if (person.getName().equals("Gina")) {
                assertEquals(10, person.getAmount());
            }
        }
    }

    @Test
    public void execute_uppercaseIdentifier_correctlyUpdatesNetOwedMap() throws PayPalsException {
        String command = "addequal D/Trip N/Eve F/Frank F/Gina A/30";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        addEqualCommand.execute(activityManager, false);

        assertEquals(1, activityManager.getSize());
        Activity activity = activityManager.getActivity(0);
        assertEquals("Trip", activity.getDescription());
        assertEquals("Eve", activity.getPayer().getName());

        Person payer = activity.getPayer();
        assertEquals(-20, payer.getAmount());
        ArrayList<Person> friends = new ArrayList<>(activity.getAllFriends());
        assertEquals(2, friends.size());

        for (Person person : friends) {
            if (person.getName().equals("Frank")) {
                assertEquals(10, person.getAmount());
            } else if (person.getName().equals("Gina")) {
                assertEquals(10, person.getAmount());
            }
        }
    }

    @Test
    public void execute_activityWithZeroAmount_exceptionThrown() {
        String command = "addequal d/Meeting n/Jake f/Karen a/0";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        try {
            addEqualCommand.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NEGATIVE_AMOUNT.getMessage(), e.getMessage());
            assertEquals(0, activityManager.getSize());
        }
    }

    /*@Test
    public void execute_validInput_activityAddedSuccessfully() {
        String command = "add d/Dinner n/Alice f/Bob a/10 f/Charlie a/20";
        AddCommand addCommand = new AddCommand(command);

        assertDoesNotThrow(() -> addCommand.execute(activityManager, false));
    }*/

    @Test
    public void execute_missingDescription_exceptionThrown() {
        String command = "addequal n/Alice f/Bob a/10";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_DESCRIPTION);
        }
    }

    @Test
    public void execute_missingPayer_exceptionThrown() {
        String command = "add d/Dinner f/Bob a/10";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_PAYER);
        }
    }

    @Test
    public void execute_missingAmount_exceptionThrown() {
        String command = "addequal d/Dinner n/Alice f/Bob";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_AMOUNT_ENTERED);
        }
    }

    /*@Test
    public void execute_multipleAmount_exceptionThrown() {
        String command = "add d/Dinner n/Alice f/Bob a/5.0 a/3.0";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.MULTIPLE_AMOUNTS_ENTERED);
        }
    }*/

    @Test
    public void execute_payerOwesThemselves_exceptionThrown() {
        String command = "addequal d/Lunch n/Bob f/Bob a/15";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.PAYER_OWES);
        }
    }

    @Test
    public void execute_payerOwesThemselvesMixedCase_exceptionThrown() {
        String command = "addequal d/Lunch n/bob f/BOB a/15";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.PAYER_OWES);
        }
    }

    @Test
    public void execute_duplicateFriendEntry_exceptionThrown() {
        String command = "addequal d/Trip n/Alice f/Bob f/Bob a/10";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.DUPLICATE_FRIEND);
        }
    }

    @Test
    public void execute_duplicateFriendEntryMixedCase_exceptionThrown() {
        String command = "d/Trip n/Alice f/bob f/BOB a/10";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.DUPLICATE_FRIEND);
        }
    }

    @Test
    public void execute_noFriends_exceptionThrown() {
        String command = "addequal d/Trip n/Alice a/10";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_FRIENDS);
        }
    }

    @Test
    public void execute_invalidAmount_exceptionThrown() {
        String command = "addequal d/Trip n/Alice f/Bob a/abcdefg";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertEquals(e.getMessage(), ExceptionMessage.INVALID_AMOUNT.getMessage());
        }
    }

    @Test
    public void execute_negativeAmount_exceptionThrown() {
        String command = "d/Trip n/Alice f/Bob a/-10.00";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NEGATIVE_AMOUNT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_largeAmount_exceptionThrown() {
        String command = "d/Trip n/Alice f/Bob a/100000.00";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException for large amount");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.LARGE_AMOUNT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_invalidMoneyFormat_exceptionThrown() {
        String command = "d/Trip n/Alice f/Bob a/10.123";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected PayPalsException for money format");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NOT_MONEY_FORMAT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_validRounding_correctRoundedAmounts() throws PayPalsException {
        String command = "d/Pizza n/Anna f/Bill f/Clara a/10";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        addEqualCommand.execute(activityManager, false);

        Activity activity = activityManager.getActivity(0);
        ArrayList<Person> friends = new ArrayList<>(activity.getAllFriends());
        for (Person p : friends) {
            assertEquals(3.33, p.getAmount(), 0.01);
        }
        assertEquals(-6.67, activity.getPayer().getAmount(), 0.01);
    }

    @Test
    public void isExit_someInput_expectFalse() {
        String command = "d/Trip n/Eve f/Frank f/Gina a/30";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);

        assertFalse(addEqualCommand.isExit(), "isExit() should return false for an AddEqualCommand");
    }

    // Test that extra trailing tokens in the amount value cause an exception (e.g., "30 extra").
    @Test
    public void execute_extraTrailingTokenInAmount_exceptionThrown() {
        String command = "addequal d/Trip n/Alice f/Bob f/Charlie a/30 extra";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected exception due to extra trailing tokens in amount");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_AMOUNT.getMessage(), e.getMessage());
        }
    }

    // Test that a friend name which is empty (or only whitespace) throws an INVALID_FRIEND exception.
    @Test
    public void execute_emptyFriendName_exceptionThrown() {
        String command = "addequal d/Trip n/Alice f/    a/30";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected exception due to empty friend name");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_FRIEND.getMessage(), e.getMessage());
        }
    }

    // Test that if a friend name equals the payer’s name, a PAYER_OWES exception is thrown.
    @Test
    public void execute_friendNameSameAsPayer_exceptionThrown() {
        String command = "addequal d/Trip n/Alice f/Alice a/30";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        try {
            addEqualCommand.execute(activityManager, false);
            fail("Expected exception since payer cannot owe themselves");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.PAYER_OWES.getMessage() + "Alice", e.getMessage());
        }
    }

    // Test that friend names with extra spaces are trimmed correctly.
    @Test
    public void execute_friendNamesWithExtraSpaces_trimmed() throws PayPalsException {
        String command = "addequal d/Trip n/Alice f/  Bob   f/   Charlie    a/60";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        addEqualCommand.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        // Check that the trimmed names exist.
        assertEquals(60/3.0, activity.getFriend("Bob").getAmount(), 0.001);
        assertEquals(60/3.0, activity.getFriend("Charlie").getAmount(), 0.001);
    }

    // Test equal split with more friends and exact division.
    // For instance, total 100 split among 4 people (3 friends + payer).
    @Test
    public void execute_equalSplit_exactDivision() throws PayPalsException {
        String command = "addequal d/Trip n/Alice f/Bob f/Charlie f/David a/100";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        addEqualCommand.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        // Each share should be 100/4 = 25.0 and payer gets -75.0.
        assertEquals(25.0, activity.getFriend("Bob").getAmount(), 0.001);
        assertEquals(25.0, activity.getFriend("Charlie").getAmount(), 0.001);
        assertEquals(25.0, activity.getFriend("David").getAmount(), 0.001);
        assertEquals(-75.0, activity.getPayer().getAmount(), 0.001);
    }

    // Test that the total amount value is properly trimmed when it has extra whitespace.
    @Test
    public void execute_totalAmountWithWhitespace_success() throws PayPalsException {
        String command = "addequal d/Trip n/Alice f/Bob f/Charlie a/   60   ";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        addEqualCommand.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        assertEquals(20.0, activity.getFriend("Bob").getAmount(), 0.001);
        assertEquals(20.0, activity.getFriend("Charlie").getAmount(), 0.001);
        assertEquals(-40.0, activity.getPayer().getAmount(), 0.001);
    }

    // Test that friend names with special characters are accepted.
    @Test
    public void execute_friendNamesWithSpecialCharacters_success() throws PayPalsException {
        String command = "addequal d/Trip n/Alice f/Bob-Smith f/Clara_Lee a/50";
        AddEqualCommand addEqualCommand = new AddEqualCommand(command);
        addEqualCommand.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        assertNotNull(activity.getFriend("Bob-Smith"));
        assertNotNull(activity.getFriend("Clara_Lee"));
    }

    @Test
    public void execute_numbersInPayerName_throwsException() {
        AddEqualCommand cmd = new AddEqualCommand("d/dinner n/123 f/Bob f/Bobby a/10");
        try {
            cmd.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NUMBERS_IN_NAME.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_numbersInFriendName_throwsException() {
        AddEqualCommand cmd = new AddEqualCommand("d/dinner n/Bob f/123 f/Clara a/10");
        try {
            cmd.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NUMBERS_IN_NAME.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_noFriends_throwsException() {
        AddEqualCommand cmd = new AddEqualCommand("d/dinner n/Bob a/28");
        try {
            cmd.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_FRIENDS.getMessage(), e.getMessage());
        }
    }
}
