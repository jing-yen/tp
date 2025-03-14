package paypals.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import paypals.Parser;
import paypals.commands.Command;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class ParserTest {
    @Test
    public void decodeCommand_invalidCommand_throwsException() {
        Parser parser = new Parser();
        String invalidCommand = "invalid command";
        try {
            Command c = parser.decodeCommand(invalidCommand);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_COMMAND.getMessage(), e.getMessage());
        }
    }
}
