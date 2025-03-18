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

    public PaidCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        try {
            UI ui = new UI(enablePrint);
            Matcher matcher = parseCommand();

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
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }
    }

    public Matcher parseCommand() throws PayPalsException {
        Pattern pattern = Pattern.compile("^n/([^\\s]+)\\s+i/(\\S+)$");
        Matcher matcher = pattern.matcher(command);

        if (!matcher.matches() || matcher.groupCount() != 2) {
            throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
        }

        assert matcher.group(1) != null : "Friend name should not be null";
        assert matcher.group(2) != null : "Activity index should not be null";

        return matcher;
    }

    public Activity getValidActivity(ActivityManager activityManager, String friendName, int index)
            throws PayPalsException {
        HashMap<String, ArrayList<Activity>> personActivitiesMap = activityManager.getPersonActivitiesMap();
        ArrayList<Activity> activities = personActivitiesMap.get(friendName);

        if (activities == null) {
            throw new PayPalsException(ExceptionMessage.INVALID_FRIEND);
        }

        if (index < 0 || index >= activities.size()) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
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
        updateNetOwedMap(activityManager, activity, friendName);
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
    //updates netOwedMap
    public void updateNetOwedMap(ActivityManager activityManager, Activity activity, String friendName) {
        HashMap<String, Double> netOwedMap = activityManager.getNetOwedMap();
        String payerName = activity.getPayer().getName();
        Person friend = activity.getFriend(friendName);
        double amount = friend.getAmount();

        updateNetAmount(netOwedMap, friendName, amount);
        updateNetAmount(netOwedMap, payerName, -amount);
    }

    //updates netOwedMap for a specific person
    public void updateNetAmount(HashMap<String, Double> netOwedMap, String name, double amount) {
        double updatedAmount = netOwedMap.getOrDefault(name, 0.0) + amount;

        if (updatedAmount == 0.0) {
            netOwedMap.remove(name);
        } else {
            netOwedMap.put(name, updatedAmount);
        }
    }

}
