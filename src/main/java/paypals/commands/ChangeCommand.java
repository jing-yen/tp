package paypals.commands;

import paypals.ActivityManager;
import paypals.util.Group;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class ChangeCommand extends Command{

    public ChangeCommand(String command) {
        super(command);
    }

    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        if (!command.isEmpty()){
            throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
        }
        activityManager.getActivityList().clear();
        Group.groupSelection();
    }
}
