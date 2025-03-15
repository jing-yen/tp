package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeleteCommand extends Command {

    public DeleteCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager) throws PayPalsException {
        String identifier;
        HashMap<String, Double> netOwedMap = activityManager.getNetOwedMap();
        // Step 1: Process the description and name
        String descRegex = "(?<=i/)(\\d+)";
        Pattern descPattern = Pattern.compile(descRegex);
        Matcher descMatcher = descPattern.matcher(command);

        if (descMatcher.find()) {
            identifier = descMatcher.group(0);
        } else {
            throw new PayPalsException(ExceptionMessage.NO_IDENTIFIER);
        }

        int id;
        try {
            id = Integer.parseInt(identifier) - 1;
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }
        if (id >= activityManager.getSize()){
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }

        Activity deletedActivity = activityManager.getActivity(id);
        Person payer = deletedActivity.getPayer();
        Map<Person, Double> owed = deletedActivity.getOwed();
        double totalOwed = 0;

        for (Person person : owed.keySet()) {
            String oweName = person.getName();
            Double oweAmount = owed.get(person);
            netOwedMap.put(oweName, netOwedMap.getOrDefault(oweName,0.0) + oweAmount);
            totalOwed += oweAmount;
        }

        String payerName = payer.getName();
        netOwedMap.put(payerName, netOwedMap.getOrDefault(payerName,0.0) - totalOwed);
        System.out.println("Expense removed successfully!");
        activityManager.deleteActivity(id);
    }
}
