package paypals.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExitCommandTest {

    @Test
    public void testIsExit() {
        ExitCommand exitCommand = new ExitCommand("");
        assert exitCommand.isExit() : "isExit() should return true for an ExitCommand";
    }

}
