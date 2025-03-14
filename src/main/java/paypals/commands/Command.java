package paypals.commands;

import paypals.ActivityManager;
import paypals.exception.PayPalsException;

public abstract class Command {
    String command;

    Command(String command) {
        this.command = command;
    }

    public abstract void execute(ActivityManager activityManager) throws PayPalsException;

    public boolean isExit() {
        return false;
    }
}
