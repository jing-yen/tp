package paypals.commands;

import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.UI;
import paypals.ActivityManager;

/**
 * Represents the HelpCommand in the PayPals application.
 * Displays a list of all available commands and their formats to the user.
 */
public class HelpCommand extends Command {

    /**
     * Constructs a HelpCommand with the given input command string.
     *
     * @param command The raw command string entered by the user.
     */
    public HelpCommand(String command) {
        super(command);
    }

    /**
     * Executes the HelpCommand.
     * Prints a list of available commands and their usage formats via the UI.
     *
     * @param activityManager The ActivityManager managing current activities (not used in this command).
     * @param enablePrint     Flag to control whether output should be printed.
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        if (!command.isEmpty()){
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, "help");
        }
        UI ui = new UI(enablePrint);
        ui.print("""
                Help - Available Commands:
                
                Commands with format:
                  1. add        -> add d/DESCRIPTION n/PAYER f/FRIEND1 a/AMOUNT_1 f/FRIEND2 a/AMOUNT_2...
                  2. addequal   -> addequal d/DESCRIPTION n/PAYER f/FRIEND1 f/FRIEND2 ... a/AMOUNT
                  3. delete     -> delete i/IDENTIFIER
                  4. paid       -> paid n/NAME i/IDENTIFIER
                  5. unpaid     -> unpaid n/NAME i/IDENTIFIER
                  6. edit       -> (Description) edit i/IDENTIFIER d/NEWDESCRIPTION
                                -> (Payer name) edit i/IDENTIFIER n/NEWNAME
                                -> (Friend name) edit i/IDENTIFIER f/NEWNAME o/OLDNAME
                                -> (Friend amount) edit i/IDENTIFIER a/NEWAMOUNT o/FRIENDNAME
                  7. list       -> list
                                -> list n/NAME
                                -> list balance n/NAME
                
                Commands without format:
                  1. split
                  2. help
                  3. exit
                  4. change
                
                For more details, please refer to the User Guide of PayPals.
                """);
    }
}
