package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.exception.ExceptionMessage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExitCommandTest extends PayPalsTest {

    @Test
    public void isExit_nullInput_expectTrue() {
        ActivityManager activityManager = new ActivityManager();
        ExitCommand exitCommand = new ExitCommand("");

        assertTrue(exitCommand.isExit(), "isExit() should return true for an ExitCommand");
        assertDoesNotThrow(()->{
            exitCommand.execute(activityManager, true);
        });
    }

    @Test
    public void isExit_withInput_expectFalse() {
        ActivityManager activityManager = new ActivityManager();
        try {
            ExitCommand exitCommand = new ExitCommand("bye bye");
            exitCommand.execute(activityManager, true);
        } catch (Exception e){
            assertException(e, ExceptionMessage.INVALID_FORMAT);
        }

    }

}
