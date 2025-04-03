package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.Activity;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AddCommandTest extends PayPalsTest {

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
            assertEquals(ExceptionMessage.INVALID_FORMAT.getMessage(), e.getMessage());
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
}

