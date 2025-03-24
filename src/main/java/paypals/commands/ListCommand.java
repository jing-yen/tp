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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static paypals.commands.PaidCommand.getActivities;

public class ListCommand extends Command {

    private static final String WRONG_LIST_FORMAT = "list n/NAME";

    private final UI ui;

    private final String spacing = "    ";
    public ListCommand(String command) {
        super(command);
        this.ui = new UI(true);
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
        UI ui = new UI(true);
        if (activityManager.getSize()==0) {
            ui.print("You currently have no activities.");
            return;
        }
        ui.print("You have " + activityManager.getSize() + " activities:");
        for (int i = 0; i < activityManager.getSize(); i++) {
            Activity activity = activityManager.getActivity(i);
            int index = i + 1;
            ui.print(index + ".  " + activity.toString());
        }
    }

    public void printPersonActivities(ActivityManager activityManager) throws PayPalsException{
        Pattern pattern = Pattern.compile("^n/([^/]+)$");
        Matcher matcher = pattern.matcher(command);
        if (!matcher.matches()) {
            Logging.logWarning("Invalid input format");
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, WRONG_LIST_FORMAT);
        }
        String name = matcher.group(1);
        ArrayList<Activity> personActivities = getPersonActivities(name,activityManager.getActivityList());

        if (personActivities.isEmpty()) {
            Logging.logWarning("Payer could not be found");
            throw new PayPalsException(ExceptionMessage.NO_PAYER);
        }
        ui.print(getListString(name,personActivities));
    }

    public String getListString(String name, ArrayList<Activity> activities) {
        StringBuilder outputString = new StringBuilder(name + ":\n");
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            int index = i + 1;
            Person friend = activity.getFriend(name);

            outputString.append(index).append(".  ");
            //name entered is the payer for the activity
            if (friend == null) {
                outputString.append(getPayerString(activity)).append("\n");
            } else {
                outputString.append("Desc: ")
                        .append(activity.getDescription()).append("\n")
                        .append(spacing).append("Payer: ").append(activity.getPayer().getName())
                        .append("\n").append(spacing).append("Amount: ").append(friend.toString(true))
                        .append("\n");
            }
        }
        return outputString.toString();
    }

    public String getPayerString(Activity activity) {
        Collection<Person> friendsCollection = activity.getAllFriends();
        StringBuilder outputString = new StringBuilder("[PAYER] Desc: " + activity.getDescription() + "\n"
                + spacing + "Owed by: ");
        for (Person friend : friendsCollection) {
            outputString.append(friend.toString(false)).append(" ");
        }
        return outputString.toString();
    }

    public ArrayList<Activity> getPersonActivities(String name, ArrayList<Activity> allActivities) {
        return getActivities(name, allActivities);
    }
}
