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
}
