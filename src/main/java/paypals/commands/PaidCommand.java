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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaidCommand extends Command {

    private static final String WRONG_PAID_FORMAT = "paid n/NAME i/IDENTIFIER";

    public PaidCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        UI ui = new UI(enablePrint);
        Matcher matcher = parseCommand();

        try {
            String friendName = matcher.group(1).trim();
            int activityIndex = Integer.parseInt(matcher.group(2)) - 1;

            Activity activity = getValidActivity(activityManager, friendName, activityIndex);
            if (validatePayment(activity, friendName)){
                markAllAsPaid(activity,activityManager);
            } else {
                markAsPaid(activity, friendName, activityManager);
            }

            ui.print("Marked as paid!");
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER, matcher.group(2));
        }
    }

    public Matcher parseCommand() throws PayPalsException {
        Pattern pattern = Pattern.compile("^n/([^\\s]+)\\s+i/(\\S+)$");
        Matcher matcher = pattern.matcher(command);

        if (!matcher.matches() || matcher.groupCount() != 2) {
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, WRONG_PAID_FORMAT);
        }

        assert matcher.group(1) != null : "Friend name should not be null";
        assert matcher.group(2) != null : "Activity index should not be null";

        return matcher;
    }

    public Activity getValidActivity(ActivityManager activityManager, String friendName, int index)
            throws PayPalsException {
        ArrayList<Activity> activities = getPersonActivities(friendName,activityManager.getActivityList());

        if (activities.isEmpty()) {
            throw new PayPalsException(ExceptionMessage.INVALID_FRIEND, friendName);
        }

        if (index < 0 || index >= activities.size()) {
            throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER, Integer.toString(index+1));
        }
        return activities.get(index);
    }

    public boolean validatePayment(Activity activity, String friendName) throws PayPalsException {
        String payerName = activity.getPayer().getName();
        Person friend = activity.getFriend(friendName);

        if (payerName.equals(friendName)) {
            return true;
        }

        if (friend.hasPaid()) {
            throw new PayPalsException(ExceptionMessage.ALREADY_PAID);
        }
        return false;
    }

    public void markAsPaid(Activity activity, String friendName, ActivityManager activityManager) {
        Person friend = activity.getFriend(friendName);
        friend.markAsPaid();
    }

    public void markAllAsPaid(Activity activity, ActivityManager activityManager) {
        Collection<Person> personCollection = activity.getAllFriends();
        for (Person friend : personCollection){
            if (friend.hasPaid()) {
                continue;
            }
            markAsPaid(activity, friend.getName(), activityManager);
        }
    }

    public ArrayList<Activity> getPersonActivities(String name, ArrayList<Activity> allActivities) {
        return getActivities(name, allActivities);
    }

    static ArrayList<Activity> getActivities(String name, ArrayList<Activity> allActivities) {
        ArrayList<Activity> personActivities = new ArrayList<>();
        for (Activity activity : allActivities) {
            if (activity.getPayer().getName().equals(name)) {
                personActivities.add(activity);
            } else {
                HashMap<String, Person> owed = activity.getOwed();
                if (owed.containsKey(name)) {
                    personActivities.add(activity);
                }
            }
        }
        return personActivities;
    }
}
