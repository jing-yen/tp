package paypals.commands;

import paypals.ActivityManager;

public class ExitCommand extends Command {
    String command;

    public ExitCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager) {

    }

    @Override
    public boolean isExit() {
        return true;
    }
}
