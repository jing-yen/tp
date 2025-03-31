package paypals.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.UI;

public class EditCommand extends Command {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("([idnfao])/\\s*([^/]+?)(?=\\s+[idnfao]/|$)");
    private static final double LARGE_AMOUNT_LIMIT = 10000.0;
    private static final String MONEY_FORMAT = "^\\d+(\\.\\d{2})?$";

    public EditCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        UI ui = new UI(enablePrint);
        Map<String, String> parameters = parseCommand();

        String id = parameters.getOrDefault("i", "");
        if (id.isEmpty()) {
            throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
        }

        try {
            int activityId = parseActivityId(id, activityManager.getSize() - 1);
            applyEdit(activityManager, ui, activityId, parameters);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }

    private Map<String, String> parseCommand() {
        Map<String, String> parameters = new HashMap<>();
        Matcher matcher = COMMAND_PATTERN.matcher(command);

        while (matcher.find()) {
            parameters.put(matcher.group(1), matcher.group(2).trim());
        }
        return parameters;
    }

    private int parseActivityId(String id, int size) throws PayPalsException {
        try {
            int activityId = Integer.parseInt(id) - 1;
            if (activityId > size || activityId < 0) {
                throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
            }
            return activityId;
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }
    }

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
            editCount++; // Name change for owed person
        }
        if (parameters.get("a") != null && parameters.get("o") != null) {
            editCount++; // Amount change for owed person
        }

        // If exactly one condition is attempted, return true, else return false.
        return (editCount == 1);
    }

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
            activityManager.editActivityPayer(activityId, payerName);
            ui.print("Payer's name has been modified to " + payerName);
        } else if (parameters.get("f") != null && parameters.get("o") != null) {
            String friend = parameters.get("f");
            String oldName = parameters.get("o");
            activityManager.editActivityOwedName(activityId, oldName, friend);
            ui.print(oldName + "'s name has been changed to " + friend);
        } else if (parameters.get("a") != null && parameters.get("o") != null) {
            try {
                String amount = parameters.get("a");
                if (!isValidAmount(amount)) {
                    throw new PayPalsException(ExceptionMessage.NOT_MONEY_FORMAT);
                }
                String name = parameters.get("o");
                double parseAmt = Double.parseDouble(amount);
                if (parseAmt > LARGE_AMOUNT_LIMIT) {
                    throw new PayPalsException(ExceptionMessage.LARGE_AMOUNT);
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

    public boolean isValidAmount(String amountStr) {
        return amountStr.matches(MONEY_FORMAT);
    }
}
