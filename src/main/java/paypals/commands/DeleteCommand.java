package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeleteCommand extends Command {

    public DeleteCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        UI ui = new UI(enablePrint);
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
        Collection<Person> owed = deletedActivity.getAllFriends();
        double totalOwed = 0;

        for (Person owedPerson : owed) {
            String personName = owedPerson.getName();
            Double updatedAmount = netOwedMap.get(personName) + owedPerson.getAmount();

            if (updatedAmount == 0.0){
                netOwedMap.remove(personName);
            } else {
                netOwedMap.put(personName, updatedAmount);
            }
        }

        String payerName = payer.getName();
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


        ui.print("Expense removed successfully!");
        activityManager.deleteActivity(id);
    }
}
