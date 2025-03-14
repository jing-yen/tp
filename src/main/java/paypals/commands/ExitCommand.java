package paypals.commands;

import paypals.ActivityManager;
import paypals.PayPals;

public class ExitCommand extends Command {
    String command;

    public ExitCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager) {
        PayPals.isExit = true;
    }
}
