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
    private static final String WRONG_DELETE_FORMAT = "delete i/IDENTIFIER";

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

        validatePrefixOrder();

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
        int count = command.split("(?i)i/").length - 1; // Count occurrences of "a/"
        if (count > 1) {
            Logging.logWarning("Multiple 'i/' prefixes found in command");
            throw new PayPalsException(ExceptionMessage.MULTIPLE_IDENTIFIER);
        } else if (count < 1) {
            throw new PayPalsException(ExceptionMessage.NO_IDENTIFIER);
        }
        if (!command.toLowerCase().startsWith("i/")) {
            throw new PayPalsException(ExceptionMessage.INVALID_DELETE_FORMAT);
        }
        return command.split("(?i)i/")[1];
        /*String identifier;
        // Step 1: Process the description and name
        String descRegex = "(?<=i/)\\S+";
        Pattern descPattern = Pattern.compile(descRegex, Pattern.CASE_INSENSITIVE);
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

    /**
     * Validates the format of flags in the command string to ensure they conform to the expected pattern.
     *
     * <p>This method checks for invalid flags in the command string where:
     * <ul>
     *   <li>A valid flag must consist of a single alphabetic character followed by a '/' (e.g., "a/", "b/").</li>
     *   <li>The content after the '/' must not contain additional '/' characters or spaces.</li>
     *   <li>Flags with two or more characters before the '/' are considered invalid (e.g., "ab/", "nn/").</li>
     * </ul>
     *
     * <p>If an invalid flag is detected, the method throws {@link PayPalsException} with an error message.
     *
     * @throws PayPalsException if the command string contains invalid flags that do not conform to expected format.
     *                          The exception includes the error message {@link ExceptionMessage#INVALID_FORMAT}
     *                          and the specific format error code {@code WRONG_DELETE_FORMAT}.
     */
    public void validatePrefixOrder() throws PayPalsException {
        // Check for incorrect flags, with 2 or more characters before the '/' character
        String regex = "(?<=\\S[a-zA-Z]\\/)([^\\/]+?)(?=\\s+[a-zA-Z]+\\/|$)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, WRONG_DELETE_FORMAT);
        }
    }
}
