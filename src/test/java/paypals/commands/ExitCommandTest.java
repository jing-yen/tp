package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.PayPalsTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitCommandTest extends PayPalsTest {

    @Test
    public void isExit_nullInput_expectTrue() {
        ExitCommand exitCommand = new ExitCommand("");
        assertTrue(exitCommand.isExit(), "isExit() should return true for an ExitCommand");
    }

    @Test
    public void isExit_withInput_expectTrue() {
        ExitCommand exitCommand = new ExitCommand("bye bye");
        assertTrue(exitCommand.isExit(), "isExit() should return true for an ExitCommand");
    }

}
