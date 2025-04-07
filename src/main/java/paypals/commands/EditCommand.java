package paypals.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.UI;

/**
 * Represents the command to edit an existing activity in PayPals.
 * Allows editing of description, payer name, owed person's name, or owed amount.
 */
public class EditCommand extends Command {
    private static final String WRONG_EDIT_FORMAT =
            "\n(Description) edit i/IDENTIFIER d/NEWDESCRIPTION\n" +
             "(Payer name) edit i/IDENTIFIER n/NEWNAME\n" +
             "(Friend name) edit i/IDENTIFIER f/NEWNAME o/OLDNAME\n" +
             "(Friend amount) edit i/IDENTIFIER a/NEWAMOUNT o/FRIENDNAME";

    /** Regex pattern for extracting parameters with prefixes like i/, d/, n/, f/, a/, o/. */
    private static final Pattern COMMAND_PATTERN = Pattern.compile(
            "([idnfao])/\\s*([^/]+?)(?=\\s+[idnfao]/|$)", Pattern.CASE_INSENSITIVE);

    /** Maximum amount limit allowed per person. */
    private static final double LARGE_AMOUNT_LIMIT = 10000.0;
    private static final String MONEY_FORMAT = "^-?\\d+(\\.\\d{1,2})?$";

    /**
     * Constructs an EditCommand object.
     *
     * @param command The full user input string.
     */
    public EditCommand(String command) {
        super(command);
    }

    /**
     * Executes the EditCommand.
     * Parses the input, validates the parameters, and applies the appropriate edit
     * to an existing activity in the ActivityManager.
     *
     * @param activityManager The manager that stores and updates activity records.
     * @param enablePrint     Flag to determine if the command should output messages via UI.
     * @throws PayPalsException If any validation or command parsing fails.
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        validatePrefixOrder();

        UI ui = new UI(enablePrint);

        if (command == null || command.trim().isEmpty()) {
            throw new PayPalsException(ExceptionMessage.EDIT_FORMAT_ERROR);
        }
        if (!isValidEditCommandOrder(command)) {
            throw new PayPalsException(ExceptionMessage.EDIT_FORMAT_ERROR);
        }

        Map<String, String> parameters = parseCommand();

        String id = parameters.get("i");
        if (id == null) {
            throw new PayPalsException(ExceptionMessage.NO_IDENTIFIER);
        }
        int activityId = parseActivityId(id, activityManager.getSize() - 1);
        applyEdit(activityManager, ui, activityId, parameters);
    }

    /**
     * Parses the command string to extract all valid parameter key-value pairs.
     *
     * @return A map of parsed parameters, keyed by their prefix letters.
     */
    private Map<String, String> parseCommand() {
        Map<String, String> parameters = new HashMap<>();
        Matcher matcher = COMMAND_PATTERN.matcher(command);

        while (matcher.find()) {
            parameters.put(matcher.group(1).toLowerCase(), matcher.group(2).trim());
        }
        return parameters;
    }

    /**
     * Converts a string identifier to an activity index.
     *
     * @param id   The identifier string provided by the user.
     * @param size The maximum valid index (based on number of activities).
     * @return The zero-based activity index.
     * @throws PayPalsException If the identifier is invalid or out of bounds.
     */
    private int parseActivityId(String id, int size) throws PayPalsException {
        try {
            int activityId = Integer.parseInt(id) - 1;
            if (activityId > size || activityId < 0) {
                throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER, id);
            }
            return activityId;
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER, id);
        }
    }

    /**
     * Checks that the command contains exactly one valid type of edit.
     *
     * @param parameters The parsed parameters from the command input.
     * @return true if the input attempts only one valid edit operation; false otherwise.
     */
    private boolean checkValidParameters(Map<String, String> parameters) {
        //Count how many valid command conditions have been fulfilled
        int editCount = 0;
        if (parameters.get("d") != null) {
            editCount++;
        }
        if (parameters.get("n") != null) {
            editCount++;
        }
        if (parameters.get("f") != null && parameters.get("o") != null) {
            editCount++; // Editing friend's name
        }
        if (parameters.get("a") != null && parameters.get("o") != null) {
            editCount++; // Editing amount owed
        }

        // If exactly one condition is attempted, return true, else return false.
        return (editCount == 1);
    }

    public boolean isValidEditCommandOrder(String command) {
        // Use the existing regex to extract parameters in order.
        Matcher matcher = COMMAND_PATTERN.matcher(command);
        StringBuilder keyOrder = new StringBuilder();
        while (matcher.find()) {
            keyOrder.append(matcher.group(1).toLowerCase());
        }
        String order = keyOrder.toString();
        // Valid orders based on allowed formats.
        return order.equals("id") || order.equals("in") ||
                order.equals("ifo") || order.equals("iao");
    }

    /**
     * Applies the corresponding edit operation on the activity identified by ID.
     *
     * @param activityManager The activity manager containing the activity list.
     * @param ui              The UI object for printing messages.
     * @param activityId      The ID of the activity to be edited.
     * @param parameters      The parameters parsed from the user command.
     * @throws PayPalsException If any edit-related validation fails.
     */
    private void applyEdit(ActivityManager activityManager, UI ui, int activityId, Map<String, String> parameters)
            throws PayPalsException {
        if (!checkValidParameters(parameters)) {
            throw new PayPalsException(ExceptionMessage.EDIT_FORMAT_ERROR);
        }

        if (parameters.get("d") != null) {
            String description = parameters.get("d");
            activityManager.editActivityDesc(activityId, description);
            ui.print("Description has been changed to " + description);

        } else if (parameters.get("n") != null) {
            String payerName = parameters.get("n");
            if (payerName.matches(".*\\d.*")) {
                throw new PayPalsException(ExceptionMessage.NUMBERS_IN_NAME);
            }
            if (activityManager.getActivity(activityId).getFriend(payerName.toLowerCase()) != null) {
                throw new PayPalsException(ExceptionMessage.PAYER_NAME_SAME_AS_FRIEND);
            }
            activityManager.editActivityPayer(activityId, payerName);
            ui.print("Payer's name has been modified to " + payerName);

        } else if (parameters.get("f") != null && parameters.get("o") != null) {
            String friend = parameters.get("f");
            if (friend.matches(".*\\d.*")) {
                throw new PayPalsException(ExceptionMessage.NUMBERS_IN_NAME);
            }
            String oldName = parameters.get("o");
            if (activityManager.getActivity(activityId).getPayer().getName().equalsIgnoreCase(friend)) {
                throw new PayPalsException(ExceptionMessage.FRIEND_NAME_SAME_AS_PAYER);
            }
            if (activityManager.getActivity(activityId).getFriend(friend.toLowerCase()) != null) {
                throw new PayPalsException(ExceptionMessage.FRIEND_NAME_SAME_AS_ANOTHER_FRIEND);
            }
            if (activityManager.getActivity(activityId).getFriend(oldName.toLowerCase()) == null) {
                throw new PayPalsException(ExceptionMessage.FRIEND_DOES_NOT_EXIST);
            }

            // Reassign oldName to correct case name too
            oldName = activityManager.editActivityOwedName(activityId, oldName, friend);
            ui.print(oldName + "'s name has been changed to " + friend);

        } else if (parameters.get("a") != null && parameters.get("o") != null) {
            try {
                String amount = parameters.get("a");
                String name = parameters.get("o");
                double parseAmt = Double.parseDouble(amount);
                if (!isValidAmount(amount)) {
                    throw new PayPalsException(ExceptionMessage.NOT_MONEY_FORMAT);
                }

                if (parseAmt > LARGE_AMOUNT_LIMIT) {
                    throw new PayPalsException(ExceptionMessage.LARGE_AMOUNT);
                }
                if (parseAmt <= 0.0) {
                    Logging.logWarning("Amount entered for friend out of bounds");
                    throw new PayPalsException(ExceptionMessage.AMOUNT_OUT_OF_BOUNDS);
                }

                if (activityManager.getActivity(activityId).getFriend(name) == null) {
                    throw new PayPalsException(ExceptionMessage.FRIEND_DOES_NOT_EXIST);
                }
                if (activityManager.getActivity(activityId).getFriend(name).hasPaid()) {
                    throw new PayPalsException(ExceptionMessage.EDIT_AMOUNT_WHEN_PAID);
                }

                activityManager.editActivityOwedAmount(activityId, name, parseAmt);
                ui.print(name + "'s amount owed has been changed to " + amount);

            } catch (NumberFormatException e) {
                throw new PayPalsException(ExceptionMessage.INVALID_AMOUNT);
            }
        } else {
            throw new PayPalsException(ExceptionMessage.EDIT_FORMAT_ERROR);
        }
    }

    /**
     * Validates whether a string is in the correct monetary format (up to two decimal places).
     *
     * @param amountStr The string representing the amount.
     * @return true if valid; false otherwise.
     */
    public boolean isValidAmount(String amountStr) {
        return amountStr.matches(MONEY_FORMAT);
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
     * <p>If an invalid flag is detected, the method throws a {@link PayPalsException} with appropriate error message.
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
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, WRONG_EDIT_FORMAT);
        }
    }
}
