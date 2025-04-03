package paypals.util;

import org.junit.jupiter.api.Test;

import paypals.PayPalsTest;
import paypals.commands.Command;
import paypals.commands.AddCommand;
import paypals.commands.AddEqualCommand;
import paypals.commands.DeleteCommand;
import paypals.commands.ListCommand;
import paypals.commands.SplitCommand;
import paypals.commands.PaidCommand;
import paypals.commands.EditCommand;
import paypals.commands.ExitCommand;
import paypals.commands.UnpaidCommand;
import paypals.commands.HelpCommand;
import paypals.commands.ChangeCommand;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest extends PayPalsTest {

    private final Parser parser = new Parser();
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

    @Test
    public void decodeCommand_validAddCommand_returnsAddCommand() throws PayPalsException {
        Command command = parser.decodeCommand("add d/Dinner n/Alice f/Bob a/10");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void decodeCommand_validAddEqualCommand_returnsAddEqualCommand() throws PayPalsException {
        Command command = parser.decodeCommand("addequal d/Lunch n/John f/Mary a/30");
        assertInstanceOf(AddEqualCommand.class, command);
    }

    @Test
    public void decodeCommand_validDeleteCommand_returnsDeleteCommand() throws PayPalsException {
        Command command = parser.decodeCommand("delete i/1");
        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    public void decodeCommand_validListCommand_returnsListCommand() throws PayPalsException {
        Command command = parser.decodeCommand("list n/Alice");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void decodeCommand_validSplitCommand_returnsSplitCommand() throws PayPalsException {
        Command command = parser.decodeCommand("split");
        assertInstanceOf(SplitCommand.class, command);
    }

    @Test
    public void decodeCommand_validPaidCommand_returnsPaidCommand() throws PayPalsException {
        Command command = parser.decodeCommand("paid n/Bob i/2");
        assertInstanceOf(PaidCommand.class, command);
    }

    @Test
    public void decodeCommand_validExitCommand_returnsExitCommand() throws PayPalsException {
        Command command = parser.decodeCommand("exit");
        assertInstanceOf(ExitCommand.class, command);
    }

    @Test
    public void decodeCommand_validHelpCommand_returnsHelpCommand() throws PayPalsException {
        Command command = parser.decodeCommand("help");
        assertInstanceOf(HelpCommand.class, command);
    }

    @Test
    public void decodeCommand_validEditCommand_returnsEditCommand() throws PayPalsException {
        Command command = parser.decodeCommand("edit i/1 n/Bob");
        assertInstanceOf(EditCommand.class, command);
    }

    @Test
    public void decodeCommand_validUnpaidCommand_returnsUnpaidCommand() throws PayPalsException {
        Command command = parser.decodeCommand("unpaid n/Bob i/1");
        assertInstanceOf(UnpaidCommand.class, command);
    }

    @Test
    public void decodeCommand_validChangeCommand_returnsChangeCommand() throws PayPalsException {
        Command command = parser.decodeCommand("change n/Alice");
        assertInstanceOf(ChangeCommand.class, command);
    }

    @Test
    public void decodeCommand_emptyInput_throwsInvalidCommand() {
        Exception e = assertThrows(PayPalsException.class, () -> parser.decodeCommand(""));
        assertEquals(ExceptionMessage.INVALID_COMMAND.getMessage(), e.getMessage());
    }

    @Test
    public void decodeCommand_commandWithoutArguments_handlesGracefully() throws PayPalsException {
        Command help = parser.decodeCommand("help");
        Command exit = parser.decodeCommand("exit");
        Command split = parser.decodeCommand("split");

        assertInstanceOf(HelpCommand.class, help);
        assertInstanceOf(ExitCommand.class, exit);
        assertInstanceOf(SplitCommand.class, split);
    }

    @Test
    public void decodeCommand_tooManySpacesInFormat_throwsInvalidCommand() {
        Exception e = assertThrows(PayPalsException.class, () -> parser.decodeCommand("  too many spaces  here"));
        assertEquals(ExceptionMessage.INVALID_COMMAND.getMessage(), e.getMessage());
    }
}
