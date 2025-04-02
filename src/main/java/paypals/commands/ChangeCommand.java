package paypals.commands;

import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import static paypals.PayPals.groupSelection;

/**
 * Represents the ChangeCommand in the PayPals application.
 * This command allows the user to switch to a different group.
 * Upon execution, the current activity list is cleared and group selection is re-initiated.
 */
public class ChangeCommand extends Command {

    /**
     * Constructs a ChangeCommand with the specified command string.
     *
     * @param command the raw command string entered by the user.
     */
    public ChangeCommand(String command) {
        super(command);
    }

    /**
     * Executes the ChangeCommand.
     * Clears the current activity list and triggers the group selection process again.
     *
     * @param activityManager The manager handling all activity-related data.
     * @param enablePrint     A flag indicating whether output should be printed (not used here).
     * @throws PayPalsException if the command is not empty (as no parameters are expected).
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        if (!command.isEmpty()) {
            throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
        }
        activityManager.getActivityList().clear();
        groupSelection();
    }
}
