package paypals.commands;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommand extends Command {

    public AddCommand(String command) {
        super(command);
    }

    public void execute(ActivityManager activityManager) throws PayPalsException {
        String description;
        String name;
        HashMap<String, Integer> owed = new HashMap<String, Integer>();

        // Step 1: Process the description and name
        String descRegex = "(?<=d\\/)(.*?)(?=\\s*[a-zA-Z]\\/\\s*)";
        Pattern descPattern = Pattern.compile(descRegex);
        Matcher descMatcher = descPattern.matcher(command);

        if (descMatcher.find()) {
            description = descMatcher.group(1);
        } else {
            throw new PayPalsException(ExceptionMessage.NO_DESCRIPTION);
        }

        String nameRegex = "(?<=n\\/)(.*?)(?=\\s*[a-zA-Z]\\/\\s*)";
        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(command);

        if (nameMatcher.find()) {
            name = nameMatcher.group(1);
        } else {
            throw new PayPalsException(ExceptionMessage.NO_PAYER);
        }

        // Step 2: Capture all (f/... a/...) pairs
        String[] pairs = command.split("(\\s+f\\/\\s*)");
        for (int i = 1; i< pairs.length; i++) {
            String[] parameters = pairs[i].split("\\s*a/");
            if (parameters.length==2) {
                String oweName = parameters[0];
                int oweAmount = Integer.parseInt(parameters[1]);
                if (name.equals(oweName)) {
                    throw new PayPalsException(ExceptionMessage.PAYER_OWES);
                }
                if (owed.containsKey(oweName)) {
                    throw new PayPalsException(ExceptionMessage.DUPLICATE_FRIEND);
                }
                owed.put(oweName, oweAmount);
            }
        }
        System.out.println("Desc: "+description);
        System.out.println("Name: "+name);
        System.out.println("Friends size: "+owed.size());
        activityManager.addActivity(new Activity(description, name, owed));
    }
}
