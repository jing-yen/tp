package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.UI;

import java.util.ArrayList;
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
            //step 1: split the command
            UI ui = new UI(enablePrint);
            Pattern pattern = Pattern.compile("^n/([^\\s]+)\\s+i/(\\S+)$");
            Matcher matcher = pattern.matcher(command);

            if (!matcher.matches() || matcher.groupCount() != 2) {

                throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
            }

            String friendName = matcher.group(1).trim();

            HashMap<String, ArrayList<Activity>> personActivitesMap = activityManager.getPersonActivitiesMap();
            ArrayList<Activity> activities = personActivitesMap.get(friendName);

            int id = Integer.parseInt(matcher.group(2)) -1;

            if (activities == null) {
                throw new PayPalsException(ExceptionMessage.INVALID_FRIEND);
            }

            if (id < 0 || id >= activities.size()) {
                throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
            }
            Activity activity = activities.get(id);

            String payerName = activity.getPayer().getName();
            Person friend = activity.getFriend(friendName);

            if (payerName.equals(friendName)){
                throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
            }
          
            if (friend.hasPaid()){
                throw new PayPalsException(ExceptionMessage.ALREADY_PAID);
            }
            friend.markAsPaid();

            HashMap<String, Double> netOwedMap = activityManager.getNetOwedMap();

            //update netOwedMap for friend
            double updatedAmount = netOwedMap.get(friendName) + friend.getAmount();
            if (updatedAmount == 0.0) {
                netOwedMap.remove(friendName);
            } else {
                netOwedMap.put(friendName, updatedAmount);
            }

            //update netOwedMap for payer
            updatedAmount = netOwedMap.get(payerName) - friend.getAmount();
            if (updatedAmount == 0.0) {
                netOwedMap.remove(payerName);
            } else {
                netOwedMap.put(payerName, updatedAmount);
            }
            ui.print("Marked as paid!");
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER);
        }
    }

}
