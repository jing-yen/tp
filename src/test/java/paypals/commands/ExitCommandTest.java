package paypals.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitCommandTest {

    @Test
    public void isExit_nullInput_expectTrue() {
        ExitCommand exitCommand = new ExitCommand("");
        assertTrue(exitCommand.isExit(), "isExit() should return true for an ExitCommand");
    }

}
