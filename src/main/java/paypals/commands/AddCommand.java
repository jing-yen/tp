package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.PersonManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommand extends Command {

    public AddCommand(String command) {
        super(command);
    }

    public void execute(ActivityManager activityManager) throws PayPalsException {
        String description;
        String name;
        HashMap<String, Double> owed = new HashMap<String, Double>();
        HashMap<String, ArrayList<Activity>> personActivitesMap = activityManager.getPersonActivitiesMap();

        // Step 1: Process the description and name
        String descRegex = "(?<=d\\/)(.*?)(?=\\s*[a-zA-Z]\\/\\s*)";
        Pattern descPattern = Pattern.compile(descRegex);
        Matcher descMatcher = descPattern.matcher(command);

        if (descMatcher.find()) {
            description = descMatcher.group(1);
        } else {
            throw new PayPalsException(ExceptionMessage.NO_DESCRIPTION);
        }

        String nameRegex = "(?<=n\\/)(.*?)(?=\\s*[a-zA-Z]\\/\\s*)";
        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(command);

        if (nameMatcher.find()) {
            name = nameMatcher.group(1).trim();
        } else {
            throw new PayPalsException(ExceptionMessage.NO_PAYER);
        }

        // Step 2: Capture all (f/... a/...) pairs
        double totalOwed = 0;
        String[] pairs = command.split("\\s+f/");
        for (int i = 1; i< pairs.length; i++) {
            String[] parameters = pairs[i].split("\\s+a/");
            if (parameters.length==2) {
                String oweName = parameters[0].trim();
                Double oweAmount = Double.parseDouble(parameters[1]);
                if (name.equals(oweName)) {
                    throw new PayPalsException(ExceptionMessage.PAYER_OWES);
                }
                if (owed.containsKey(oweName)) {
                    throw new PayPalsException(ExceptionMessage.DUPLICATE_FRIEND);
                }
                owed.put(oweName, oweAmount);
                activityManager.getNetOwedMap().put(oweName, activityManager.getNetOwedMap().getOrDefault(oweName,0.0) - oweAmount);
                totalOwed += oweAmount;
            }
        }
        activityManager.getNetOwedMap().put(name, activityManager.getNetOwedMap().getOrDefault(name,0.0) + totalOwed);
        System.out.println("Desc: "+description);
        System.out.println("Name: "+name);
        System.out.println("Friends size: "+owed.size());
        Activity newActivity = new Activity(description, name, owed);
        activityManager.addActivity(newActivity);

        //Map each friend to the activity
        for (Map.Entry<String, Double> entry : owed.entrySet()){
            ArrayList<Activity> activitiesList =
                    personActivitesMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());

            activitiesList.add(newActivity);
        }
        //Map the payer to the activity
        ArrayList<Activity> activitiesList = personActivitesMap.computeIfAbsent(name, k -> new ArrayList<>());

        activitiesList.add(newActivity);
    }
}
