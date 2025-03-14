package paypals;

import paypals.commands.AddCommand;
import paypals.commands.Command;
import paypals.commands.DeleteCommand;
import paypals.commands.ExitCommand;
import paypals.commands.ListCommand;
import paypals.commands.PaidCommand;
import paypals.commands.SplitCommand;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class Parser {
    public Command decodeCommand(String command) throws PayPalsException {
        String[] tokens = command.split(" ", 2);
        switch (tokens[0]) {
        case "add":
            return new AddCommand(tokens[1]);
        case "delete":
            return new DeleteCommand(tokens[1]);
        case "list":
            return new ListCommand(tokens[1]);
        case "split":
            return new SplitCommand("");
        case "paid":
            return new PaidCommand(tokens[1]);
        case "exit":
            return new ExitCommand("");
        default:
            throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
        }
    }
}
