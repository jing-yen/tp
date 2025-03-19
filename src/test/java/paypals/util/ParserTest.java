package paypals.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

import paypals.PayPalsTest;
import paypals.commands.Command;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class ParserTest extends PayPalsTest {
    @Test
    public void decodeCommand_invalidCommand_throwsException() {
        Parser parser = new Parser();
        String invalidCommand = "invalid command";
        try {
            Command c = parser.decodeCommand(invalidCommand);
            fail();
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.INVALID_COMMAND);
        }
    }
}
