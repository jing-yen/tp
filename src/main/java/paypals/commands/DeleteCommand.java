package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;
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
        String payerName = deletedActivity.getPayer();
        Map<String, Double> owed = deletedActivity.getOwed();
        double totalOwed = 0;

        for (Map.Entry<String, Double> element: owed.entrySet()) {
            String personName = element.getKey();
            Double owedAmount = element.getValue();
            Double updatedAmount = netOwedMap.get(personName) + owedAmount;

            if (updatedAmount == 0.0){
                netOwedMap.remove(personName);
            } else {
                netOwedMap.put(personName, updatedAmount);
            }
        }

        netOwedMap.put(payerName, netOwedMap.getOrDefault(payerName,0.0) - totalOwed);

        HashMap<String, ArrayList<Activity>> personActivitiesMap = activityManager.getPersonActivitiesMap();
        ArrayList<String> personToRemove = new ArrayList<>();

        for (Map.Entry<String, ArrayList<Activity>> entry : personActivitiesMap.entrySet()) {
            ArrayList<Activity> activitiesList = entry.getValue();
            activitiesList.remove(deletedActivity);

            if (activitiesList.isEmpty()) {
                personToRemove.add(entry.getKey());
            }
        }
        for (String personName : personToRemove) {
            personActivitiesMap.remove(personName);
        }


        System.out.println("Expense removed successfully!");
        activityManager.deleteActivity(id);
    }
}
