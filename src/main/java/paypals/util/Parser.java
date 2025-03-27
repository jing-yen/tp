package paypals.util;

import paypals.commands.AddCommand;
import paypals.commands.AddEqualCommand;
import paypals.commands.Command;
import paypals.commands.DeleteCommand;
import paypals.commands.ListCommand;
import paypals.commands.SplitCommand;
import paypals.commands.PaidCommand;
import paypals.commands.ExitCommand;
import paypals.commands.HelpCommand;
import paypals.commands.EditCommand;
import paypals.commands.UnpaidCommand;
import paypals.commands.Command;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.Map;

public class Parser {

    private static final Map<String, String> COMMAND_EXAMPLE =
            Map.of("add", "add d/DESCRIPTION n/PAYER f/FRIEND1 a/AMOUNT_OWED_1 f/FRIEND2 a/AMOUNT_OWED_2...",
                    "delete", "delete i/IDENTIFIER",
                    "paid", "paid n/NAME i/IDENTIFIER");

    public Command decodeCommand(String input) throws PayPalsException {
        String[] tokens = input.split(" ", 2);
        if (tokens.length != 2 && tokens.length != 1) {
            throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
        }
        String command = tokens[0].trim();
        String parameters = "";
        if (tokens.length == 2) {
            parameters = tokens[1].trim();
        }
        try {
            switch (command) {
            case "add":
                return new AddCommand(parameters);
            case "addequal":
                return new AddEqualCommand(parameters);
            case "delete":
                return new DeleteCommand(parameters);
            case "list":
                return new ListCommand(parameters);
            case "split":
                return new SplitCommand("");
            case "paid":
                return new PaidCommand(parameters);
            case "exit":
                return new ExitCommand("");
            case "help":
                return new HelpCommand("");
            case "edit":
                return new EditCommand(parameters);
            case "unpaid":
                return new UnpaidCommand(parameters);
            default:
                throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, COMMAND_EXAMPLE.get(command));
        }
    }
}
