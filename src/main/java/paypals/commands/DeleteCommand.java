package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.UI;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeleteCommand extends Command {


    public DeleteCommand(String command) {
        super(command);
    }


    @Override
    public void execute(ActivityManager activityManager, boolean enablePrint) throws PayPalsException {
        assert activityManager != null : "ActivityManager should not be null";

        Logging.logInfo("Executing DeleteCommand with command: " + command);
        UI ui = new UI(enablePrint);
        String identifier = getIdentifier();
        int id = getID(identifier, activityManager.getSize());
        assert id == Integer.parseInt(identifier) - 1 : "ID should match the identifier - 1";
        Activity deletedActivity = activityManager.getActivity(id);
        Collection<Person> owed = deletedActivity.getAllFriends();
        boolean hasPaid = false;
        ui.print("Expense removed successfully!");
        activityManager.deleteActivity(id);
        Logging.logInfo("Activity with id " + id + " has been deleted from ActivityManager.");
    }

    private String getIdentifier() throws PayPalsException {
        String identifier;
        // Step 1: Process the description and name
        String descRegex = "(?<=i/)(\\d+)";
        Pattern descPattern = Pattern.compile(descRegex);
        Matcher descMatcher = descPattern.matcher(command);

        if (descMatcher.find()) {
            identifier = descMatcher.group(0);
            Logging.logInfo("Identifier found: " + identifier);
        } else {
            Logging.logWarning("No identifier found in command: " + command);
            throw new PayPalsException(ExceptionMessage.NO_IDENTIFIER);
        }
        return identifier;
    }

    private int getID(String identifier, int size) throws PayPalsException {
        int id;
        try {
            id = Integer.parseInt(identifier) - 1;
        } catch (NumberFormatException e) {
            Logging.logWarning("Invalid identifier format: " + identifier);
            throw new PayPalsException(ExceptionMessage.INVALID_IDENTIFIER, identifier);
        }

        boolean idIsTooLarge = id >= size;
        boolean idIsTooSmall = id < 0;

        if (idIsTooLarge || idIsTooSmall) {
            Logging.logWarning("Identifier out of bounds: id=" + id + ", size=" + size);
            throw new PayPalsException(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER, Integer.toString(id+1));
        }
        return id;
    }
}
