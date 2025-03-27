package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static paypals.ActivityManager.getActivities;

/**
 * Handles the "unpaid" command to mark an activity as unpaid for a specific person,
 * or everyone if the requester is the payer.
 */
public class UnpaidCommand extends Command {

    private static final String WRONG_PAID_FORMAT = "unpaid n/NAME i/IDENTIFIER";

    /**
     * Constructs an UnpaidCommand with the given command string.
     *
     * @param command the input command in the format "n/NAME i/IDENTIFIER"
     */
    public UnpaidCommand(String command) {
        super(command);
    }

    /**
     * Executes the unpaid command, marking an activity as unpaid either for a friend
     * or all friends if the friend is the payer.
     *
     * @param activityManager the activity manager containing all activities
     * @param enablePrint     whether to enable printing to UI
     * @throws PayPalsException if parsing fails or payment state is already unpaid
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        UI ui = new UI(enablePrint);
        Matcher matcher = parseCommand();

        try {
            String friendName = matcher.group(1).trim();
            int activityIndex = Integer.parseInt(matcher.group(2)) - 1;

            Activity activity = getValidActivity(activityManager, friendName, activityIndex);
            if (validatePayment(activity, friendName)) {
                markAllAsUnpaid(activity, activityManager);
            } else {
                markAsUnpaid(activity, friendName, activityManager);
            }

            ui.print("Marked as unpaid!");
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER, matcher.group(2));
        }
    }

    /**
     * Parses the command and extracts the name and activity index using regex.
     *
     * @return the matcher containing the parsed groups
     * @throws PayPalsException if format is invalid
     */
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

    /**
     * Gets a list of activities that are fully paid by the given person.
     *
     * @param activities the list of activities to check
     * @param name       the name of the person
     * @return a list of activities marked as paid by that person
     */
    public ArrayList<Activity> getPaidArray(ArrayList<Activity> activities, String name) {
        ArrayList<Activity> paid = new ArrayList<>();
        for (Activity activity : activities) {
            if (activity.isActivityFullyPaid(name, false)) {
                paid.add(activity);
            }
        }
        return paid;
    }

    /**
     * Validates and retrieves an activity at the given index from the person's paid activities.
     *
     * @param activityManager the activity manager
     * @param friendName      the person's name
     * @param index           the index of the activity in their paid list
     * @return the selected activity
     * @throws PayPalsException if friend not found or index is out of bounds
     */
    public Activity getValidActivity(ActivityManager activityManager, String friendName, int index)
            throws PayPalsException {
        ArrayList<Activity> activities = getPersonActivities(friendName, activityManager.getActivityList());
        if (activities.isEmpty()) {
            throw new PayPalsException(ExceptionMessage.INVALID_FRIEND, friendName);
        }

        ArrayList<Activity> paid = getPaidArray(activities, friendName);

        if (index < 0 || index >= paid.size()) {
            throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER, Integer.toString(index + 1));
        }
        return paid.get(index);
    }

    /**
     * Validates whether the person can be marked as unpaid.
     *
     * @param activity   the activity in question
     * @param friendName the name of the friend
     * @return true if the friend is the payer, otherwise false
     * @throws PayPalsException if already unpaid
     */
    public boolean validatePayment(Activity activity, String friendName) throws PayPalsException {
        String payerName = activity.getPayer().getName();
        Person friend = activity.getFriend(friendName);

        if (payerName.equals(friendName)) {
            return true;
        }

        if (!friend.hasPaid()) {
            throw new PayPalsException(ExceptionMessage.ALREADY_UNPAID);
        }
        return false;
    }

    /**
     * Marks the given friend as unpaid in the specified activity.
     *
     * @param activity        the activity to update
     * @param friendName      the name of the friend to mark as unpaid
     * @param activityManager the activity manager
     */
    public void markAsUnpaid(Activity activity, String friendName, ActivityManager activityManager) {
        Person friend = activity.getFriend(friendName);
        friend.markAsUnpaid();
    }

    /**
     * Marks all friends as unpaid in the specified activity (when the payer makes the command).
     *
     * @param activity        the activity to update
     * @param activityManager the activity manager
     */
    public void markAllAsUnpaid(Activity activity, ActivityManager activityManager) {
        Collection<Person> personCollection = activity.getAllFriends();
        for (Person friend : personCollection) {
            if (!friend.hasPaid()) {
                continue;
            }
            markAsUnpaid(activity, friend.getName(), activityManager);
        }
    }

    /**
     * Retrieves all activities involving the specified person.
     *
     * @param name          the person's name
     * @param allActivities all available activities
     * @return the list of activities involving the person
     */
    public ArrayList<Activity> getPersonActivities(String name, ArrayList<Activity> allActivities) {
        return getActivities(name, allActivities);
    }
}
