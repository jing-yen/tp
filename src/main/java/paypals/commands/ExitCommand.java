package paypals.commands;

import paypals.ActivityManager;

public class ExitCommand extends Command {
    String command;

    public ExitCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) {
        System.out.println("Thank you for using Paypals!");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
