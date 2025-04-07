package paypals.commands;

import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.UI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command that handles the deletion of an activity from the activity manager.
 * The command processes an identifier in the format "i/[number]" to determine
 * which activity to delete.
 */
public class DeleteCommand extends Command {

    /**
     * Constructs a new DeleteCommand with the specified command string.
     *
     * @param command The command string containing the identifier
     */
    public DeleteCommand(String command) {
        super(command);
    }

    /**
     * Executes the delete command by removing the specified activity from the activity manager.
     * The method extracts the identifier from the command, validates it, converts it to an
     * index, and deletes the corresponding activity.
     *
     * @param activityManager The activity manager containing the list of activities
     * @param enablePrint Boolean flag indicating whether to print output messages
     * @throws PayPalsException If the identifier is missing, invalid, or out of bounds
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        assert activityManager != null : "ActivityManager should not be null";

        Logging.logInfo("Executing DeleteCommand with command: " + command);
        String identifier = getIdentifier();
        int id = getID(identifier, activityManager.getSize());
        assert id == Integer.parseInt(identifier) - 1 : "ID should match the identifier - 1";
        activityManager.deleteActivity(id);
        UI ui = new UI(enablePrint);
        ui.print("Expense removed successfully!");
        Logging.logInfo("Activity with id " + id + " has been deleted from ActivityManager.");
    }

    /**
     * Extracts the identifier from the command string using regex pattern matching.
     * The identifier is expected to be in the format "i/[number]".
     *
     * @return The extracted identifier as a string
     * @throws PayPalsException If no identifier is found in the command string
     */
    private String getIdentifier() throws PayPalsException {
        int count = command.split("i/").length - 1; // Count occurrences of "a/"
        if (count > 1) {
            Logging.logWarning("Multiple 'i/' prefixes found in command");
            throw new PayPalsException(ExceptionMessage.MULTIPLE_IDENTIFIER);
        } else if (count < 1) {
            throw new PayPalsException(ExceptionMessage.NO_IDENTIFIER);
        }
        if (!command.startsWith("i/")) {
            throw new PayPalsException(ExceptionMessage.INVALID_DELETE_FORMAT);
        }
        return command.split("i/")[1];
        /*String identifier;
        // Step 1: Process the description and name
        String descRegex = "(?<=i/)\\S+";
        Pattern descPattern = Pattern.compile(descRegex);
        Matcher descMatcher = descPattern.matcher(command);
        if (descMatcher.find()) {
            identifier = descMatcher.group(0);
            Logging.logInfo("Identifier found: " + identifier);
        } else {
            Logging.logWarning("No identifier found in command: " + command);
            throw new PayPalsException(ExceptionMessage.NO_IDENTIFIER);
        }
        return identifier;*/
    }

    /**
     * Converts the identifier string to an integer ID and validates that it's within
     * the bounds of the activity list.
     *
     * @param identifier The string identifier extracted from the command
     * @param size The number of activities in the activity manager
     * @return The validated activity ID (zero-based index)
     * @throws PayPalsException If the identifier cannot be parsed as an integer
     *                          or if the resulting ID is out of bounds
     */
    private int getID(String identifier, int size) throws PayPalsException {
        int id;
        try {
            id = Integer.parseInt(identifier) - 1;
        } catch (NumberFormatException e) {
            Logging.logWarning("Invalid identifier format: " + identifier);
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER, identifier);
        }

        boolean idIsTooLarge = id >= size;
        boolean idIsTooSmall = id < 0;

        if (idIsTooLarge || idIsTooSmall) {
            Logging.logWarning("Identifier out of bounds: id=" + id + ", size=" + size);
            throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER, Integer.toString(id+1));
        }
        return id;
    }
}
