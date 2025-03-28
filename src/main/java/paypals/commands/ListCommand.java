package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.UI;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static paypals.ActivityManager.getActivities;

/**
 * Handles the "list" command in PayPals, used to list all activities
 * or activities related to a specific person.
 */
public class ListCommand extends Command {

    private static final String WRONG_LIST_FORMAT = "list n/NAME";
    private static final Pattern NAME_PATTERN = Pattern.compile("^n/([^/]+)$");
    private static final String INDENT = "    ";

    private final UI ui;

    /**
     * Constructs a ListCommand with the specified command string.
     *
     * @param command the command string, e.g., "" or "n/NAME"
     */
    public ListCommand(String command) {
        super(command);
        this.ui = new UI(true);
    }

    /**
     * Executes the list command based on whether a name is provided.
     *
     * @param activityManager the manager holding all activities
     * @param enablePrint     whether to enable printing
     * @throws PayPalsException if the command format is invalid or the person is not found
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        if (command.isEmpty()) {
            printAllActivities(activityManager);
        } else {
            printPersonActivities(activityManager);
        }
    }

    /**
     * Prints all activities
     *
     * @param activityManager the manager containing all activities
     */
    private void printAllActivities(ActivityManager activityManager) {
        if (activityManager.getSize() == 0) {
            ui.print("You currently have no activities.");
            return;
        }

        ui.print("You have " + activityManager.getSize() + " activities:");

        ArrayList<Activity> allActivities = activityManager.getActivityList();
        printActivities(allActivities);
    }

    /**
     * Prints activities related to a specific person, split into paid and unpaid categories.
     *
     * @param activityManager the manager containing all activities
     * @throws PayPalsException if the command format is invalid or the person is not found
     */
    private void printPersonActivities(ActivityManager activityManager) throws PayPalsException {
        Matcher matcher = NAME_PATTERN.matcher(command);
        if (!matcher.matches()) {
            Logging.logWarning("Invalid input format");
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, WRONG_LIST_FORMAT);
        }

        String name = matcher.group(1);
        ArrayList<Activity> personActivities = getActivities(name, activityManager.getActivityList());

        if (personActivities.isEmpty()) {
            Logging.logWarning("Payer could not be found");
            throw new PayPalsException(ExceptionMessage.NO_PAYER);
        }

        ArrayList<Activity> paid = new ArrayList<>();
        ArrayList<Activity> unpaid = new ArrayList<>();

        for (Activity activity : personActivities) {
            if (activity.isActivityFullyPaid(name, false)) {
                paid.add(activity);
            } else {
                unpaid.add(activity);
            }
        }

        ui.print("Activities which have been fully paid for " + name + ":");
        ui.print(formatActivitiesList(paid, name));

        ui.print("Activities which have not been fully paid for " + name + ":");
        ui.print(formatActivitiesList(unpaid, name));
    }

    /**
     * Prints a list of activities, each preceded by an index number.
     *
     * @param activities the list of activities to print
     */
    private void printActivities(ArrayList<Activity> activities) {
        for (int i = 0; i < activities.size(); i++) {
            ui.print((i + 1) + ".  " + activities.get(i));
        }
    }

    /**
     * Formats a list of activities related to a person for printing.
     *
     * @param activities the list of activities
     * @param name       the name of the person
     * @return the formatted string of activities
     */
    private String formatActivitiesList(ArrayList<Activity> activities, String name) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            Person friend = activity.getFriend(name);

            output.append(i + 1).append(".  ");
            if (friend == null) {
                output.append(formatPayerActivity(activity)).append("\n");
            } else {
                output.append("Desc: ").append(activity.getDescription()).append("\n")
                        .append(INDENT).append("Payer: ").append(activity.getPayer().getName()).append("\n")
                        .append(INDENT).append("Amount: ").append(friend.toString(true)).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * Formats the activity string for a payer.
     *
     * @param activity the activity to format
     * @return the formatted string
     */
    private String formatPayerActivity(Activity activity) {
        StringBuilder result = new StringBuilder("[PAYER] Desc: ")
                .append(activity.getDescription()).append("\n")
                .append(INDENT).append("Owed by: ");
        for (Person friend : activity.getAllFriends()) {
            result.append(friend.toString(false)).append(" ");
        }
        return result.toString().trim();
    }
}
