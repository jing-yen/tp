package paypals.commands;

import paypals.ActivityManager;
import paypals.exception.PayPalsException;

public class PaidCommand extends Command {
    String command;

    public PaidCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager) throws PayPalsException {

    }

}
