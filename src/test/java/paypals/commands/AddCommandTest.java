package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.Activity;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AddCommandTest extends PayPalsTest {

    @Test
    public void execute_validMultipleFriends_correctlyUpdatesNetOwedMap() throws PayPalsException {
        String command = "add d/Trip n/Eve f/Frank a/30 f/Gina a/20";
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
            String command = "add d/Meeting n/Jake f/Karen a/0";
            AddCommand addCommand = new AddCommand(command);
        } catch (Exception e){
            assertException(e,ExceptionMessage.AMOUNT_OUT_OF_BOUNDS);
        }
    }

    @Test
    public void execute_validInput_activityAddedSuccessfully() {
        String command = "add d/Dinner n/Alice f/Bob a/10 f/Charlie a/20";
        AddCommand addCommand = new AddCommand(command);

        assertDoesNotThrow(() -> addCommand.execute(activityManager, false));
    }

    @Test
    public void execute_missingDescription_exceptionThrown() {
        String command = "add n/Alice f/Bob a/10";
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
        String command = "add d/Dinner f/Bob a/10";
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
        String command = "add d/Dinner n/Alice f/Bob";
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
        String command = "add d/Dinner n/Alice f/Bob a/5.0 a/3.0";
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
        String command = "add d/Lunch n/Bob f/Bob a/15";
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
        String command = "add d/Trip n/Alice f/Bob a/20 f/Bob a/10";
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
        String command = "add d/Lunch n/Bob f/Bobby a/test";
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
        String command = "add d/Lunch n/Bob f/Bobby a/9999999";
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
        String command = "add d/Lunch n/Bob f/Bobby a/-10";
        AddCommand ac = new AddCommand(command);
        try {
            ac.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.AMOUNT_OUT_OF_BOUNDS.getMessage(), e.getMessage());
        }
    }
}

