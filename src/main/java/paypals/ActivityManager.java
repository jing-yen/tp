package paypals;

import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ActivityManager {
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Person> netOwed =  new ArrayList<>();

    public void executeAddCommand(String command) throws PayPalsException {
        String description, name;
        HashMap<String, Integer> owed = new HashMap<String, Integer>();

        // Step 1: Process the description and name
        String descRegex = "(?<=d\\/\s*)(.*?)(?=\\s*[a-zA-Z]\\/\\s*)";
        Pattern descPattern = Pattern.compile(descRegex);
        Matcher descMatcher = descPattern.matcher(command);

        if (descMatcher.find()) {
            description = descMatcher.group(1);
        } else {
            throw new PayPalsException(ExceptionMessage.NO_DESCRIPTION);
        }

        String nameRegex = "(?<=n\\/\\s*)(.*?)(?=\\s*[a-zA-Z]\\/\\s*)";
        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(command);

        if (nameMatcher.find()) {
            name = nameMatcher.group(1);
        } else {
            throw new PayPalsException(ExceptionMessage.NO_PAYER);
        }

        // Step 2: Capture all (f/... a/...) pairs
        String[] chunks = command.split("(\\s*f\\/\\s*)");
        for (int i=1; i<chunks.length; i++) {
            String[] chunks2 = chunks[i].split("\\s*a/");
            if (chunks2.length==2) {
                String oweName = chunks2[0];
                int oweAmount = Integer.parseInt(chunks2[1]);
                if (name.equals(oweName)) {
                    throw new PayPalsException(ExceptionMessage.PAYER_OWES);
                }
                if (owed.containsKey(oweName)) {
                    throw new PayPalsException(ExceptionMessage.DUPLICATE_FRIEND);
                }
                owed.put(chunks[0], Integer.parseInt(chunks[1]));
            }
        }
        activities.add(new Activity(description, name, owed));
    }

    public void executeDeleteCommand(int index){}

    public void executeListCommand(String command){}

    public void executeSplitCommand() {
        // Create a copy of netOwed and sort it (creditors first, debtors last)
        ArrayList<Person> netOwedArrayCopy = new ArrayList<>(netOwed);
        netOwedArrayCopy.sort(Comparator.comparingDouble(Person::getAmount).reversed());

        System.out.println("Best way to settle debts:");

        int leftIndex = 0;
        int rightIndex = netOwedArrayCopy.size() - 1;

        while (leftIndex < rightIndex) {
            Person leftPerson = netOwedArrayCopy.get(leftIndex);  // Creditor
            Person rightPerson = netOwedArrayCopy.get(rightIndex); // Debtor

            double leftAmount = leftPerson.getAmount();
            double rightAmount = Math.abs(rightPerson.getAmount());

            if (leftAmount > rightAmount) { // Creditor has more than debtor can pay
                leftPerson.setAmount(leftAmount - rightAmount);
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " " + rightAmount);
                rightIndex--; // Move to next debtor
            } else if (leftAmount < rightAmount) { // Debtor still owes after paying
                rightPerson.setAmount(-(rightAmount - leftAmount)); // Keep it negative
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " " + leftAmount);
                leftIndex++; // Move to next creditor
            } else { // Exact match
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " " + rightAmount);
                leftIndex++;
                rightIndex--;
            }
        }
    }

}
