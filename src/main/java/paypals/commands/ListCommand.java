package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListCommand extends Command {
    public ListCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
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
        System.out.println(getListString(name,activities));
    }

    public String getListString(String name, ArrayList<Activity> activities) {
        String outputString = name + ":\n";
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            int index = i + 1;
            Person friend = activity.getFriend(name);

            outputString += index + ". ";
            //name entered is the payer for the activity
            if (friend == null) {
                outputString += getPayerString(activity) + "\n";
            } else {
                outputString += activity.getDescription() + ", Payer: " +
                        activity.getPayer().getName() + ", Amount: "
                        + friend.toString(true) + "\n";
            }
        }
        return outputString;
    }

    public String getPayerString(Activity activity) {
        Collection<Person> friendsCollection = activity.getAllFriends();
        String outputString = "[PAYER] " + activity.getDescription() + ", Owed by: ";
        for (Person friend : friendsCollection) {
            outputString += friend.toString(false) + " ";
        }
        return outputString;
    }
}
