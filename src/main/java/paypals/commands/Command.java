package paypals.commands;

import paypals.ActivityManager;
import paypals.exception.PayPalsException;

public class Command {
    String command;

    Command(String command) {
        this.command = command;
    }

    public void execute(ActivityManager activityManager) throws PayPalsException {

    }
}
