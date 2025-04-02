package paypals.commands;

import paypals.ActivityManager;

/**
 * Represents the ExitCommand in the PayPals application.
 * This command signals the termination of the program.
 */
public class ExitCommand extends Command {
    String command;

    /**
     * Constructs an ExitCommand with the given command string.
     *
     * @param command The raw command string entered by the user.
     */
    public ExitCommand(String command) {
        super(command);
    }

    /**
     * Executes the ExitCommand.
     * This command does not perform any specific logic other than asserting the exit condition.
     *
     * @param activityManager The ActivityManager managing current activities (not used in this command).
     * @param enablePrint     Flag to control whether output should be printed (not used here).
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) {
        assert isExit() : "isExit should be true";
    }

    /**
     * Returns true to indicate that this command should terminate the application.
     *
     * @return true indicating exit.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
