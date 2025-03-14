package paypals.commands;

import paypals.ActivityManager;
import paypals.exception.PayPalsException;

public class DeleteCommand extends Command {
    String command;

    public DeleteCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager) throws PayPalsException {

    }
}
