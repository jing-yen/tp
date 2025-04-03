package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.PayPalsTest;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import static org.junit.jupiter.api.Assertions.*;

class ChangeCommandTest extends PayPalsTest {

    @Test
    public void execute_invalidCommand_throwsException() {
        ChangeCommand changeCommand = new ChangeCommand("test");
        PayPalsException thrown = assertThrows(PayPalsException.class, () -> {
            changeCommand.execute(activityManager, false);
        });

        assertEquals(ExceptionMessage.INVALID_COMMAND.getMessage(), thrown.getMessage());
    }

    @Test
    public void isExit_someInput_expectFalse() {
        ChangeCommand changeCommand = new ChangeCommand("");

        assertFalse(changeCommand.isExit(), "isExit() should return false for a ChangeCommand");
    }
}
