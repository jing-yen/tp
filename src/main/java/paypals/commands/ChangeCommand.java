package paypals.commands;

import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import static paypals.PayPals.groupSelection;

public class ChangeCommand extends Command{

    public ChangeCommand(String command) {
        super(command);
    }

    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        if (!command.isEmpty()){
            throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
        }
        activityManager.getActivityList().clear();
        groupSelection();
    }
}
