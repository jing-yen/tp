package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.UI;

import java.util.Arrays;
import java.util.HashMap;

public class AddEqualCommand extends AddCommand {

    private static final String WRONG_ADD_FORMAT =
            "Format: addequal d/DESCRIPTION n/PAYER f/FRIEND1 f/FRIEND2 ... a/AMOUNT_OWED";

    public AddEqualCommand(String command) {
        super(command);
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

        double totalAmount = getTotalAmount();
        assert totalAmount >= 0 : "Total amount should not be less than 0";

        String friendsPart = command.split("a/")[0];  // Get everything before 'a/'
        String[] friends = friendsPart.split("\\s+f/");
        if (friends.length <= 1) {
            throw new PayPalsException(ExceptionMessage.NO_FRIENDS, null);
        }
        friends = Arrays.copyOfRange(friends, 1, friends.length);  // Remove the command data

        double amount = totalAmount/ (friends.length + 1);

        for (String friend : friends) {
            String friendName = friend.trim();
            validateFriend(name, friendName, owed);
            owed.put(friendName, amount);
            Logging.logInfo("Friend added successfully");
        }

        ui.print("Desc: "+description);
        ui.print("Name of payer: "+name);
        ui.print("Number of friends who owe " + name +": "+owed.size());
        Activity newActivity = new Activity(description, new Person(name, -(totalAmount - amount), false), owed);
        activityManager.addActivity(newActivity);

        Logging.logInfo("Activity added successfully");

    }

    private double getTotalAmount() throws PayPalsException {
        String amountEntered = extractValue("a/", ExceptionMessage.NO_AMOUNT_ENTERED);
        double totalAmount;
        try {
            totalAmount = Double.parseDouble(amountEntered);
        } catch (NumberFormatException e) {
            Logging.logWarning("Invalid amount entered: " + amountEntered);
            throw new PayPalsException(ExceptionMessage.INVALID_AMOUNT, amountEntered);
        }
        if (totalAmount < 0) {
            throw new PayPalsException(ExceptionMessage.NEGATIVE_AMOUNT, amountEntered);
        }
        return totalAmount;
    }
}
