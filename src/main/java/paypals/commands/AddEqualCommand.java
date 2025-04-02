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
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AddEqualCommand extends AddCommand {

    private static final String WRONG_ADD_FORMAT =
            "Format: addequal d/DESCRIPTION n/PAYER f/FRIEND1 f/FRIEND2 ... a/AMOUNT_OWED";
    private static final double LARGE_AMOUNT_LIMIT = 10000.0;
    private static final String MONEY_FORMAT = "^-?\\d+(\\.\\d{1,2})?$";
    
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
        BigDecimal bdAmount = new BigDecimal(Double.toString(amount));
        bdAmount = bdAmount.setScale(2, RoundingMode.HALF_EVEN);
        double roundedAmount = bdAmount.doubleValue();


        for (String friend : friends) {
            String friendName = friend.trim();
            validateFriend(name, friendName, owed);
            owed.put(friendName, roundedAmount);
            Logging.logInfo("Friend added successfully");
        }

        ui.print("Desc: "+description);
        ui.print("Name of payer: "+name);
        ui.print("Number of friends who owe " + name +": "+owed.size());
        Activity newActivity = new Activity(description, new Person(name, -(totalAmount - roundedAmount), false), owed);
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
            throw new PayPalsException(ExceptionMessage.INVALID_AMOUNT);
        }
        if (!isValidAmount(amountEntered)) {
            throw new PayPalsException(ExceptionMessage.NOT_MONEY_FORMAT);
        }
        if (totalAmount > LARGE_AMOUNT_LIMIT) {
            Logging.logWarning("Amount entered for friend out of bounds");
            throw new PayPalsException(ExceptionMessage.LARGE_AMOUNT);
        }
        if (totalAmount <= 0) {
            throw new PayPalsException(ExceptionMessage.NEGATIVE_AMOUNT);
        }
        return totalAmount;
    }

    public boolean isValidAmount(String amountStr) {
        return amountStr.matches(MONEY_FORMAT);
    }
}
