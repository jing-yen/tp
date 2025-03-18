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

public class PaidCommand extends Command {

    public PaidCommand(String command) {
        super(command);
    }

    @Override
    public void execute(ActivityManager activityManager) throws PayPalsException {
        try {
            //step 1: split the command
            Pattern pattern = Pattern.compile("^n/([^\\s]+)\\s+i/(\\S+)$");
            Matcher matcher = pattern.matcher(command);

            if (!matcher.matches() || matcher.groupCount() != 2) {

                throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
            }

            String name = matcher.group(1).trim();

            HashMap<String, ArrayList<Activity>> personActivitesMap = activityManager.getPersonActivitiesMap();
            ArrayList<Activity> activities = personActivitesMap.get(name);

            if (activities == null) {
                throw new PayPalsException(ExceptionMessage.INVALID_FRIEND);
            }

            int id = Integer.parseInt(matcher.group(2)) -1;
            if (id < 0 || id >= activities.size()) {
                throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
            }
            Activity activity = activities.get(id);

            Person friend = activity.getFriend(name);
            if (friend == null) {
                throw new PayPalsException(ExceptionMessage.PERSON_IS_PAYER);
            }
            if (friend.hasPaid()){
                throw new PayPalsException(ExceptionMessage.ALREADY_PAID);
            }
            friend.markAsPaid();

            HashMap<String, Double> netOwedMap = activityManager.getNetOwedMap();

            double updatedAmount = netOwedMap.get(name) + friend.getAmount();
            if (updatedAmount == 0.0) {
                netOwedMap.remove(name);
            } else {
                netOwedMap.put(name, updatedAmount);
            }

            System.out.println("Marked as paid!");
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }
    }

}
