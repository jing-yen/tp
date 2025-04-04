package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.Activity;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Parser;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AddCommandTest extends PayPalsTest {

    private static final String WRONG_ADD_FORMAT =
            "add d/DESCRIPTION n/PAYER f/FRIEND1 a/AMOUNT_OWED_1 f/FRIEND2 a/AMOUNT_OWED_2...";

    @Test
    public void execute_validMultipleFriends_correctlyUpdatesNetOwedMap() throws PayPalsException {
        String command = "d/Trip n/Eve f/Frank a/30 f/Gina a/20";
        AddCommand addCommand = new AddCommand(command);
        addCommand.execute(activityManager, false);

        assertEquals(1, activityManager.getSize());
        Activity activity = activityManager.getActivity(0);
        assertEquals("Trip", activity.getDescription());
        assertEquals("Eve", activity.getPayer().getName());

        ArrayList<Person> friends = new ArrayList<>(activity.getAllFriends());
        assertEquals(2, friends.size());

        for (Person person : friends) {
            if (person.getName().equals("Frank")) {
                assertEquals(30, person.getAmount());
            } else if (person.getName().equals("Gina")) {
                assertEquals(20, person.getAmount());
            }
        }
    }

    @Test
    public void execute_activityWithZeroAmount_exceptionThrown() {
        try {
            String command = "d/Meeting n/Jake f/Karen a/0";
            AddCommand addCommand = new AddCommand(command);
        } catch (Exception e){
            assertException(e,ExceptionMessage.AMOUNT_OUT_OF_BOUNDS);
        }
    }

    @Test
    public void execute_validInput_activityAddedSuccessfully() {
        String command = "d/Dinner n/Alice f/Bob a/10 f/Charlie a/20";
        AddCommand addCommand = new AddCommand(command);

        assertDoesNotThrow(() -> addCommand.execute(activityManager, false));
    }

    @Test
    public void execute_missingDescription_exceptionThrown() {
        String command = "n/Alice f/Bob a/10";
        AddCommand addCommand = new AddCommand(command);

        try {
            addCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_DESCRIPTION);
        }
    }

    @Test
    public void execute_missingPayer_exceptionThrown() {
        String command = "d/Dinner f/Bob a/10";
        AddCommand addCommand = new AddCommand(command);

        try {
            addCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_PAYER);
        }
    }

    @Test
    public void execute_missingAmount_exceptionThrown() {
        String command = "d/Dinner n/Alice f/Bob";
        AddCommand addCommand = new AddCommand(command);

        try {
            addCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_AMOUNT_ENTERED);
        }
    }

    @Test
    public void execute_multipleAmount_exceptionThrown() {
        String command = "d/Dinner n/Alice f/Bob a/5.0 a/3.0";
        AddCommand addCommand = new AddCommand(command);

        try {
            addCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.MULTIPLE_AMOUNTS_ENTERED);
        }
    }

    @Test
    public void execute_payerOwesThemselves_exceptionThrown() {
        String command = "d/Lunch n/Bob f/Bob a/15";
        AddCommand addCommand = new AddCommand(command);

        try {
            addCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.PAYER_OWES);
        }
    }

    @Test
    public void execute_duplicateFriendEntry_exceptionThrown() {
        String command = "d/Trip n/Alice f/Bob a/20 f/Bob a/10";
        AddCommand addCommand = new AddCommand(command);

        try {
            addCommand.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.DUPLICATE_FRIEND);
        }
    }

    @Test
    public void execute_invalidAmount_throwsException() {
        String command = "d/Lunch n/Bob f/Bobby a/test";
        AddCommand ac = new AddCommand(command);
        try {
            ac.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_AMOUNT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_largeAmount_throwsException() {
        String command = "d/Lunch n/Bob f/Bobby a/9999999";
        AddCommand ac = new AddCommand(command);
        try {
            ac.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.LARGE_AMOUNT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_negativeAmount_throwsException() {
        String command = "d/Lunch n/Bob f/Bobby a/-10";
        AddCommand ac = new AddCommand(command);
        try {
            ac.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.AMOUNT_OUT_OF_BOUNDS.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_validAmountWithOneDecimal_parsedCorrectly() throws PayPalsException {
        String command = "d/Brunch n/Linda f/Mike a/7.5";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);

        Activity activity = activityManager.getActivity(0);
        Person friend = activity.getFriend("Mike");
        assertEquals(7.5, friend.getAmount(), 0.001);
    }

    @Test
    public void execute_validAmountWithTwoDecimals_parsedCorrectly() throws PayPalsException {
        String command = "d/Brunch n/Linda f/Mike a/7.55";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);

        Activity activity = activityManager.getActivity(0);
        Person friend = activity.getFriend("Mike");
        assertEquals(7.55, friend.getAmount(), 0.001);
    }

    @Test
    public void amountWithThreeDecimals_notMoneyFormat_exceptionThrown() {
        String command = "d/Trip n/Alice f/Bob a/10.555";
        AddCommand cmd = new AddCommand(command);
        try {
            cmd.execute(activityManager, false);
            fail("Expected NOT_MONEY_FORMAT exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NOT_MONEY_FORMAT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_amountWithWhitespace_validAfterTrimming() throws PayPalsException {
        String command = "d/Snacks n/Kevin f/Tim a/ 12.00 ";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);

        Person friend = activityManager.getActivity(0).getFriend("Tim");
        assertEquals(12.00, friend.getAmount(), 0.001);
    }

    @Test
    public void execute_emptyFriendName_exceptionThrown() {
        String command = "d/Picnic n/John f/ a/5.00";
        AddCommand cmd = new AddCommand(command);
        try {
            cmd.execute(activityManager, false);
            fail("Expected NO_PAYER or PAYER_OWES or input format exception");
        } catch (PayPalsException e) {
            assertTrue(
                    e.getMessage().equals(ExceptionMessage.INVALID_FRIEND.getMessage())
            );
        }
    }

    @Test
    public void execute_extraSpacesBetweenPrefixes_validCommandExecutes() throws PayPalsException {
        String command = "d/Taxi     n/Maria     f/Tom     a/10";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);

        Activity activity = activityManager.getActivity(0);
        assertEquals("Maria", activity.getPayer().getName());
        assertEquals(1, activity.getAllFriends().size());
    }

    @Test
    public void unorderedPrefixes_invalidFormat_exceptionThrown() {
        String command = "f/Bob a/10 d/Lunch n/Alice";
        AddCommand cmd = new AddCommand(command);
        try {
            cmd.execute(activityManager, false);
            fail("Expected NO_DESCRIPTION exception due to unordered prefix usage");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_FORMAT.getMessage() + WRONG_ADD_FORMAT, e.getMessage());
        }
    }

    @Test
    public void isValidAmount_validInteger_returnsTrue() {
        AddCommand cmd = new AddCommand("d/X n/Y f/Z a/10");
        assertTrue(cmd.isValidAmount("10"));
    }

    @Test
    public void isValidAmount_validDecimal_returnsTrue() {
        AddCommand cmd = new AddCommand("d/X n/Y f/Z a/10.55");
        assertTrue(cmd.isValidAmount("10.55"));
    }

    @Test
    public void isValidAmount_invalidFormat_returnsFalse() {
        AddCommand cmd = new AddCommand("d/X n/Y f/Z a/10.555");
        assertFalse(cmd.isValidAmount("10.555"));
    }

    @Test
    public void extractValue_missingPrefix_throwsException() {
        String command = "d/Dinner f/Bob a/10";
        AddCommand cmd = new AddCommand(command);
        try {
            cmd.extractValue("n/", ExceptionMessage.NO_PAYER);
            fail("Expected NO_PAYER exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_PAYER.getMessage(), e.getMessage());
        }
    }

    // Test that a description with multiple words is correctly parsed.
    @Test
    public void execute_descriptionWithSpaces_parsedCorrectly() throws PayPalsException {
        String command = "d/Group Trip n/Anna f/Ben a/25";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        assertEquals("Group Trip", activity.getDescription());
    }

    // Test that a friend name containing special characters (e.g., hyphen) is accepted.
    @Test
    public void execute_friendNameWithSpecialCharacters_validCommandExecutes() throws PayPalsException {
        String command = "d/Concert n/Mark f/Jean-Pierre a/40";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        Person friend = activity.getFriend("Jean-Pierre");
        assertNotNull(friend);
        assertEquals(40, friend.getAmount(), 0.001);
    }

    // Test that friend names differing only by case are treated as distinct (if allowed).
    @Test
    public void execute_duplicateFriendDifferentCase_validCommandExecutes() throws PayPalsException {
        String command = "d/Outing n/Alice f/Bob a/30 f/bob a/20";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        // Both "Bob" and "bob" should be present as separate entries.
        Person friend1 = activity.getFriend("Bob");
        Person friend2 = activity.getFriend("bob");
        assertNotNull(friend1);
        assertNotNull(friend2);
        assertEquals(30, friend1.getAmount(), 0.001);
        assertEquals(20, friend2.getAmount(), 0.001);
        assertEquals(2, activity.getAllFriends().size());
    }

    // Test the isValidAmount method returns false for an empty string.
    @Test
    public void isValidAmount_emptyString_returnsFalse() {
        AddCommand cmd = new AddCommand("d/Trip n/Mark f/Bob a/10");
        assertFalse(cmd.isValidAmount(""));
    }

    // Test that extracting a value which is only whitespace throws the expected exception.
    @Test
    public void extractValue_whitespaceOnlyValue_throwsException() {
        Parser parser = new Parser();
        String command = "add d/Party n/John f/Jill a/    ";
        try {
            Command cmd = parser.decodeCommand(command);
            cmd.execute(activityManager, false);
            fail("Expected NO_AMOUNT_ENTERED exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_AMOUNT_ENTERED.getMessage(), e.getMessage());
        }
    }

    // Test that an extra, unprefixed token at the end of the command results in an exception.
    @Test
    public void execute_extraTrailingToken_exceptionThrown() {
        Parser parser = new Parser();
        String command = "add d/Trip n/Alice f/Bob a/20 extra";
        try {
            Command cmd = parser.decodeCommand(command);
            cmd.execute(activityManager, false);
            fail("Expected INVALID_FORMAT exception due to extra trailing token");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_FORMAT.getMessage() + WRONG_ADD_FORMAT, e.getMessage());
        }
    }

    // Test that an empty command (no input) throws an exception.
    @Test
    public void execute_emptyInput_exceptionThrown() {
        String command = "";
        try {
            AddCommand cmd = new AddCommand(command);
            cmd.execute(activityManager, false);
            fail("Expected exception due to empty input");
        } catch (PayPalsException e) {
            // Depending on implementation, missing description may be flagged.
            assertEquals(ExceptionMessage.NO_DESCRIPTION.getMessage(), e.getMessage());
        }
    }

    // Test that an empty description (d/ with no text) throws a NO_DESCRIPTION exception.
    @Test
    public void execute_emptyDescription_exceptionThrown() {
        String command = "d/ n/Alice f/Bob a/10";
        AddCommand cmd = new AddCommand(command);
        try {
            cmd.execute(activityManager, false);
            fail("Expected NO_DESCRIPTION exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_DESCRIPTION.getMessage(), e.getMessage());
        }
    }

    // Test that an empty payer (n/ with no text) throws a NO_PAYER exception.
    @Test
    public void execute_emptyPayer_exceptionThrown() {
        String command = "d/Trip n/ f/Bob a/10";
        AddCommand cmd = new AddCommand(command);
        try {
            cmd.execute(activityManager, false);
            fail("Expected NO_PAYER exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_PAYER.getMessage(), e.getMessage());
        }
    }

    // Test that friend names with extra whitespace are trimmed correctly.
    @Test
    public void execute_friendNameWithWhitespace_validAfterTrimming() throws PayPalsException {
        String command = "d/Party n/Jane f/   Bob   a/15";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        Person friend = activity.getFriend("Bob");
        assertNotNull(friend);
        assertEquals(15, friend.getAmount(), 0.001);
    }

    // Test that payer names with extra whitespace are trimmed correctly.
    @Test
    public void execute_payerNameWithWhitespace_validAfterTrimming() throws PayPalsException {
        String command = "d/Party n/   John   f/Jill a/25";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);
        Activity activity = activityManager.getActivity(0);
        assertEquals("John", activity.getPayer().getName());
    }

    // Test that an amount with a leading plus sign is considered invalid.
    @Test
    public void isValidAmount_leadingPlusSign_returnsFalse() {
        AddCommand cmd = new AddCommand("d/Trip n/Mark f/Bob a/+10");
        assertFalse(cmd.isValidAmount("+10"));
    }

    // Test that an amount exactly equal to the limit (10000) is accepted.
    @Test
    public void execute_amountAtLimit_parsedCorrectly() throws PayPalsException {
        String command = "d/LimitTest n/John f/Jane a/10000";
        AddCommand cmd = new AddCommand(command);
        cmd.execute(activityManager, false);

        Activity activity = activityManager.getActivity(0);
        Person friend = activity.getFriend("Jane");
        assertNotNull(friend);
        assertEquals(10000, friend.getAmount(), 0.001);
    }

    // Test that an amount just above the limit (e.g., 10000.01) is rejected.
    @Test
    public void execute_amountJustAboveLimit_exceptionThrown() {
        String command = "d/LimitTest n/John f/Jane a/10000.01";
        AddCommand cmd = new AddCommand(command);
        try {
            cmd.execute(activityManager, false);
            fail("Expected LARGE_AMOUNT exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.LARGE_AMOUNT.getMessage(), e.getMessage());
        }
    }

    // Test that isValidAmount returns true for a negative number
    // (even though business logic later rejects negative amounts).
    @Test
    public void isValidAmount_negativeNumber_returnsTrue() {
        AddCommand cmd = new AddCommand("d/Trip n/Mark f/Bob a/-10");
        // The regex allows a negative sign, so this should return true.
        assertTrue(cmd.isValidAmount("-10"));
    }
}

