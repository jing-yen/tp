package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListCommand extends Command {
    public ListCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager) throws PayPalsException {
        if (command.isEmpty()) {
            printAllActivities(activityManager);
        } else {
            printPersonActivities(activityManager);
        }
    }

    public void printAllActivities(ActivityManager activityManager) {
        if (activityManager.getSize()==0) {
            System.out.println("You currently have no activities.");
        }
        for (int i = 0; i < activityManager.getSize(); i++) {
            Activity activity = activityManager.getActivity(i);
            int index = i + 1;
            System.out.println(index + ". " + activity.toString());
        }
    }

    public void printPersonActivities(ActivityManager activityManager) throws PayPalsException{
        HashMap<String, ArrayList<Activity>> personActivitiesMap = activityManager.getPersonActivitiesMap();
        Pattern pattern = Pattern.compile("^n/([^/]+)$");
        Matcher matcher = pattern.matcher(command);
        if (!matcher.matches()) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }
        String name = matcher.group(1);
        ArrayList<Activity> activities = personActivitiesMap.get(name);
        if (activities == null) {
            throw new PayPalsException(ExceptionMessage.NO_PAYER);
        }
        String outputString = name + ":\n";
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            Person friend = activity.getFriend(name);
            int index = i + 1;
            outputString += index + ". " + friend.toString(true) + " " +
                    activity.getDescription() + " to " + activity.getPayer().getName() + "\n";
        }
        System.out.println(outputString);
    }
}
