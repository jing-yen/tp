package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.UI;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommand extends Command {

    private static final String WRONG_ADD_FORMAT =
            "Format: add d/DESCRIPTION n/PAYER f/FRIEND1 a/AMOUNT_OWED_1 f/FRIEND2 a/AMOUNT_OWED_2...";
    private static final double LARGE_AMOUNT_LIMIT = 10000.0;
    private static final String MONEY_FORMAT = "^-?\\d+(\\.\\d{1,2})?$";

    public AddCommand(String command) {
        super(command);
    }

    String extractValue(String key, ExceptionMessage exceptionMessage) throws PayPalsException {
        String regex = key + "\\s*([^/]+?)(?=\\s+[a-zA-Z]/|$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            Logging.logWarning("Invalid input format detected");
            System.out.println(WRONG_ADD_FORMAT);
            throw new PayPalsException(exceptionMessage);
        }
    }

    void validateFriend(String payer, String oweName, HashMap<String, Double> owedMap) throws PayPalsException {
        assert payer != null : "Payer name should not be null";
        assert oweName != null : "OweName should not be null";
        assert owedMap != null : "Owed map should not be null";

        if (payer.equals(oweName)) {
            Logging.logWarning("Payer tried to owe themselves.");
            throw new PayPalsException(ExceptionMessage.PAYER_OWES, oweName);
        }
        if (owedMap.containsKey(oweName)) {
            Logging.logWarning("Duplicate friend entry detected: {0} already exists.");
            System.out.println(WRONG_ADD_FORMAT);
            throw new PayPalsException(ExceptionMessage.DUPLICATE_FRIEND, oweName);
        }
    }

    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        assert activityManager != null : "ActivityManager should not be null";

        UI ui = new UI(enablePrint);
        HashMap<String, Double> owed = new HashMap<String, Double>();

        // Step 1: Extract description and payer name
        String description = extractValue("d/", ExceptionMessage.NO_DESCRIPTION);
        String name = extractValue("n/", ExceptionMessage.NO_PAYER);

        assert !description.isEmpty() : "Description should not be null or empty";
        assert !name.isEmpty() : "Payer name should not be null or empty";

        // Step 2: Capture all (f/... a/...) pairs
        String[] pairs = command.split("\\s+f/");
        double totalOwed = 0.0;
        for (int i = 1; i< pairs.length; i++) {
            String[] parameters = pairs[i].split("\\s+a/");
            if (parameters.length==2) {
                String oweName = parameters[0].trim();
                double oweAmount;
                try {
                    oweAmount = Double.parseDouble(parameters[1]);
                } catch (Exception e) {
                    Logging.logWarning("Invalid amount entered for friend");
                    throw new PayPalsException(ExceptionMessage.INVALID_AMOUNT);
                }
                if (!isValidAmount(parameters[1].trim())) {
                    throw new PayPalsException(ExceptionMessage.NOT_MONEY_FORMAT);
                }
                if (oweAmount <= 0.0) {
                    Logging.logWarning("Amount entered for friend out of bounds");
                    throw new PayPalsException(ExceptionMessage.AMOUNT_OUT_OF_BOUNDS);
                }
                if (oweAmount > LARGE_AMOUNT_LIMIT) {
                    Logging.logWarning("Amount entered for friend out of bounds");
                    throw new PayPalsException(ExceptionMessage.LARGE_AMOUNT);
                }
                validateFriend(name, oweName, owed);
                owed.put(oweName, oweAmount);
                totalOwed += oweAmount;

                Logging.logInfo("Friend added successfully");
            } else {
                Logging.logWarning("Incorrect number of parameters detected: {0}");
                throw new PayPalsException(parameters.length < 2
                        ? ExceptionMessage.NO_AMOUNT_ENTERED: ExceptionMessage.MULTIPLE_AMOUNTS_ENTERED);
            }
        }

        assert totalOwed >= 0 : "Total owed amount should not be negative";

        ui.print("Desc: "+description);
        ui.print("Name of payer: "+name);
        ui.print("Number of friends who owe " + name +": "+owed.size());
        Activity newActivity = new Activity(description, new Person(name, -totalOwed, false), owed);
        activityManager.addActivity(newActivity);

        Logging.logInfo("Activity added successfully");
    }

    public boolean isValidAmount(String amountStr) {
        return amountStr.matches(MONEY_FORMAT);
    }
}
