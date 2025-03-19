package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
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
        Logging.logInfo("Executing DeleteCommand with command: " + command);
        UI ui = new UI(enablePrint);
        String identifier;
        HashMap<String, Double> netOwedMap = activityManager.getNetOwedMap();
        // Step 1: Process the description and name
        String descRegex = "(?<=i/)(\\d+)";
        Pattern descPattern = Pattern.compile(descRegex);
        Matcher descMatcher = descPattern.matcher(command);

        if (descMatcher.find()) {
            identifier = descMatcher.group(0);
            Logging.logInfo("Identifier found: " + identifier);
        } else {
            Logging.logWarning("No identifier found in command: " + command);
            throw new PayPalsException(ExceptionMessage.NO_IDENTIFIER);
        }

        int id;
        try {
            id = Integer.parseInt(identifier) - 1;
        } catch (NumberFormatException e) {
            Logging.logWarning("Invalid identifier format: " + identifier);
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }

        boolean IDisTooLarge = id >= activityManager.getSize();
        boolean IDisTooSmall = id < 0;

        if (IDisTooLarge | IDisTooSmall) {
            Logging.logWarning("Identifier out of bounds: id=" + id + ", size=" + activityManager.getSize());
            throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }

        assert id == Integer.parseInt(identifier) - 1 : "ID should match the identifier - 1";

        Activity deletedActivity = activityManager.getActivity(id);
        Person payer = deletedActivity.getPayer();
        Collection<Person> owed = deletedActivity.getAllFriends();
        double totalOwed = 0;

        for (Person owedPerson : owed) {
            String personName = owedPerson.getName();
            totalOwed += owedPerson.getAmount();
            Double updatedAmount = netOwedMap.get(personName) + owedPerson.getAmount();
            Logging.logInfo("Updating " + personName + ": current=" + netOwedMap.get(personName)
                    + ", change=" + owedPerson.getAmount() + ", new=" + updatedAmount);
            if (updatedAmount == 0.0){
                netOwedMap.remove(personName);
                Logging.logInfo("Removed " + personName + " from netOwedMap due to zero balance.");
            } else {
                netOwedMap.put(personName, updatedAmount);
            }
        }

        String payerName = payer.getName();
        Double updatedAmount = netOwedMap.get(payerName) - totalOwed;
        if (updatedAmount == 0.0){
            netOwedMap.remove(payerName);
            Logging.logInfo("Removed " + payerName + " from netOwedMap due to zero balance.");
        } else {
            netOwedMap.put(payerName, updatedAmount);
        }

        Logging.logInfo("Updated payer " + payerName + ", new balance=" + netOwedMap.get(payerName));
        HashMap<String, ArrayList<Activity>> personActivitiesMap = activityManager.getPersonActivitiesMap();
        ArrayList<String> personToRemove = new ArrayList<>();

        for (Map.Entry<String, ArrayList<Activity>> entry : personActivitiesMap.entrySet()) {
            ArrayList<Activity> activitiesList = entry.getValue();
            activitiesList.remove(deletedActivity);

            if (activitiesList.isEmpty()) {
                personToRemove.add(entry.getKey());
                Logging.logInfo("Removed " + entry.getKey() + " from personActivitiesMap (activity list empty).");
            }
        }
        for (String personName : personToRemove) {
            personActivitiesMap.remove(personName);
            Logging.logInfo("Removed " + personName + " from personActivitiesMap.");
        }


        ui.print("Expense removed successfully!");
        activityManager.deleteActivity(id);
        Logging.logInfo("Activity with id " + id + " has been deleted from ActivityManager.");
    }
}
