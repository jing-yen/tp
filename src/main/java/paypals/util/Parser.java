package paypals.util;

import paypals.commands.AddCommand;
import paypals.commands.Command;
import paypals.commands.DeleteCommand;
import paypals.commands.ExitCommand;
import paypals.commands.ListCommand;
import paypals.commands.PaidCommand;
import paypals.commands.SplitCommand;
import paypals.commands.HelpCommand;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.Map;

public class Parser {

    final Map<String, String> COMMAND_EXAMPLE =
            Map.of("add", "add d/DESCRIPTION n/PAYER f/FRIEND1 a/AMOUNT_OWED_1 f/FRIEND2 a/AMOUNT_OWED_2...",
                    "delete", "delete i/IDENTIFIER",
                    "paid", "paid n/NAME i/IDENTIFIER");

    public Command decodeCommand(String command) throws PayPalsException {
        String[] tokens = command.split(" ", 2);
        try {
            switch (tokens[0]) {
            case "add":
                return new AddCommand(tokens[1]);
            case "delete":
                return new DeleteCommand(tokens[1]);
            case "list":
                return tokens.length == 1 ? new ListCommand("") : new ListCommand(tokens[1]);
            case "split":
                return new SplitCommand("");
            case "paid":
                return new PaidCommand(tokens[1]);
            case "exit":
                return new ExitCommand("");
            case "help":
                return new HelpCommand("");
            default:
                throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, COMMAND_EXAMPLE.get(tokens[0]));
        }
    }
}
