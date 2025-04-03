package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.ActivityManager;
import paypals.PayPalsTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void isExit_withInput_expectTrue() {
        ActivityManager activityManager = new ActivityManager();
        ExitCommand exitCommand = new ExitCommand("bye bye");

        assertTrue(exitCommand.isExit(), "isExit() should return true for an ExitCommand");
        assertDoesNotThrow(()->{
            exitCommand.execute(activityManager, true);
        });
    }

}
