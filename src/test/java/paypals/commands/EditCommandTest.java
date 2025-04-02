package paypals.commands;

import paypals.PayPalsTest;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EditCommandTest extends PayPalsTest {

    @BeforeEach
    public void setUpEditTests() throws PayPalsException {
        AddCommand c1 = new AddCommand("add d/lunch n/Jane f/John a/28");
        c1.execute(activityManager, false);
    }

    @Test
    public void execute_emptyString_exceptionThrown() {
        String command = "";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_COMMAND.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_editDescription_success() {
        // Edit the description of the first activity (index 1, which maps to index 0).
        String command = "i/1 d/NewLunch";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify the description is updated.
        assertEquals("NewLunch", activityManager.getActivity(0).getDescription());
    }

    @Test
    public void execute_editPayer_success() {
        // Edit the payer name of the first activity.
        String command = "i/1 n/NewJane";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that the payer's name is updated.
        assertEquals("NewJane", activityManager.getActivity(0).getPayer().getName());
    }

    @Test
    public void execute_editFriendName_success() {
        // Change the owed friend's name: from "John" to "NewJohn" in the first activity.
        String command = "i/1 f/Johnny o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that "John" is replaced with "NewJohn".
        assertNull(activityManager.getActivity(0).getFriend("John"), "Old friend name should be removed");
        assertNotNull(activityManager.getActivity(0).getFriend("Johnny"), "New friend name should exist");
    }

    @Test
    public void execute_editFriendAmount_success() {
        // Change the amount owed by friend "John" to 10 for the first activity.
        String command = "i/1 a/10 o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that John's owed amount is updated to 35.
        assertEquals(10.0, activityManager.getActivity(0).getFriend("John").getAmount(), 0.001);
    }

    @Test
    public void execute_editFriendAmountWhenPaid_exceptionThrown() {
        // Mark friend "John" as having paid.
        String paidCommand = "n/John i/1";
        PaidCommand pc = new PaidCommand(paidCommand);
        assertDoesNotThrow(() -> pc.execute(activityManager, false));

        String command = "i/1 a/35 o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_AMOUNT_WHEN_PAID.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_invalidParameters_exceptionThrown() {
        String command = "i/1 d/Desc p/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_missingId_throwsException() {
        String command = "d/Desc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_invalidId_throwsException() {
        String command = "i/test d/Desc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_outOfBoundsId_throwsException() {
        String command = "i/-1 d/Desc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER.getMessage(), e.getMessage());
        }
    }
}
