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
 * Represents the "paid" command, used to mark an activity as paid for a specific person
 * or everyone if the requester is the payer.
 */
public class PaidCommand extends Command {

    private static final String WRONG_PAID_FORMAT = "paid n/NAME i/IDENTIFIER";

    /**
     * Constructs a PaidCommand with the specified command string.
     *
     * @param command the command string in the form "n/NAME i/IDENTIFIER"
     */
    public PaidCommand(String command) {
        super(command);
    }

    /**
     * Executes the paid command: marks the activity as paid for the friend
     * or for all friends if the friend is the payer.
     *
     * @param activityManager the activity manager containing all activities
     * @param enablePrint     whether to print output to the UI
     * @throws PayPalsException if the input is invalid or the activity is not found
     */
    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        UI ui = new UI(enablePrint);
        Matcher matcher = parseCommand();

        try {
            String friendName = matcher.group(1).trim().toLowerCase();
            int activityIndex = Integer.parseInt(matcher.group(2)) - 1;

            Activity activity = getValidActivity(activityManager, friendName, activityIndex);
            if (validatePayment(activity, friendName)) {
                markAllAsPaid(activity, activityManager);
            } else {
                markAsPaid(activity, friendName, activityManager);
            }

            ui.print("Marked as paid!");
        } catch (NumberFormatException e) {
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER, matcher.group(2));
        }
    }

    /**
     * Parses the command string using regex and extracts the name and identifier.
     *
     * @return a Matcher object with extracted groups
     * @throws PayPalsException if the command format is invalid
     */
    public Matcher parseCommand() throws PayPalsException {
        Pattern pattern = Pattern.compile("^n/([^\\s]+)\\s+i/(\\S+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);

        if (!matcher.matches() || matcher.groupCount() != 2) {
            throw new PayPalsException(ExceptionMessage.INVALID_FORMAT, WRONG_PAID_FORMAT);
        }

        assert matcher.group(1) != null : "Friend name should not be null";
        assert matcher.group(2) != null : "Activity index should not be null";

        return matcher;
    }

    /**
     * Filters a list of activities to only include those that are not fully paid by the person.
     *
     * @param activities the list of activities
     * @param name       the person's name
     * @return a list of unpaid activities for the person
     */
    public ArrayList<Activity> getUnpaidArray(ArrayList<Activity> activities, String name) {
        ArrayList<Activity> unpaid = new ArrayList<>();
        for (Activity activity : activities) {
            if (!activity.isActivityFullyPaid(name, false)) {
                unpaid.add(activity);
            }
        }
        return unpaid;
    }

    /**
     * Retrieves a valid activity object based on the person's name and activity index.
     *
     * @param activityManager the activity manager
     * @param friendName      the name of the friend
     * @param index           the index of the unpaid activity
     * @return the activity object
     * @throws PayPalsException if no activities exist or index is out of bounds
     */
    public Activity getValidActivity(ActivityManager activityManager, String friendName, int index)
            throws PayPalsException {
        ArrayList<Activity> activities = getPersonActivities(friendName, activityManager.getActivityList());
        if (activities.isEmpty()) {
            throw new PayPalsException(ExceptionMessage.INVALID_FRIEND, friendName);
        }

        ArrayList<Activity> unpaid = getUnpaidArray(activities, friendName);

        if (index < 0 || index >= unpaid.size()) {
            throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER, Integer.toString(index + 1));
        }
        return unpaid.get(index);
    }

    /**
     * Validates whether the payment can be made for the given activity.
     *
     * @param activity   the activity to validate
     * @param friendName the name of the friend making the payment
     * @return true if the payer is the friend, false if the payee is the friend
     * @throws PayPalsException if the friend has already paid
     */
    public boolean validatePayment(Activity activity, String friendName) throws PayPalsException {
        String payerName = activity.getPayer().getName();
        Person friend = activity.getFriend(friendName);

        if (payerName.equalsIgnoreCase(friendName)) {
            return true;
        }

        if (friend.hasPaid()) {
            throw new PayPalsException(ExceptionMessage.ALREADY_PAID);
        }
        return false;
    }

    /**
     * Marks the activity as paid for a specific friend.
     *
     * @param activity        the activity to mark
     * @param friendName      the name of the friend
     * @param activityManager the activity manager
     */
    public void markAsPaid(Activity activity, String friendName, ActivityManager activityManager) {
        Person friend = activity.getFriend(friendName);
        friend.markAsPaid();
    }

    /**
     * Marks all friends in the activity as paid (when the payer calls the command).
     *
     * @param activity        the activity to update
     * @param activityManager the activity manager
     */
    public void markAllAsPaid(Activity activity, ActivityManager activityManager) {
        Collection<Person> personCollection = activity.getAllFriends();
        for (Person friend : personCollection) {
            if (friend.hasPaid()) {
                continue;
            }
            markAsPaid(activity, friend.getName(), activityManager);
        }
    }

    /**
     * Retrieves all activities associated with a specific person.
     *
     * @param name          the name of the person
     * @param allActivities the list of all activities
     * @return the list of activities involving the person
     */
    public ArrayList<Activity> getPersonActivities(String name, ArrayList<Activity> allActivities) {
        return getActivities(name, allActivities);
    }

}
