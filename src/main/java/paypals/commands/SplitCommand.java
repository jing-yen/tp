package paypals.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.util.UI;

/**
 * Represents the SplitCommand in PayPals.
 * This command calculates and displays the most efficient way
 * to settle all outstanding debts among participants with the fewest number of transactions.
 */
public class SplitCommand extends Command {
    String command;

    /**
     * Constructs a SplitCommand with the given command string.
     *
     * @param command the raw command string entered by the user
     */
    public SplitCommand(String command) {
        super(command);
    }

    /**
     * Executes the SplitCommand by calculating and printing the optimal settlement plan.
     *
     * @param activityManager the ActivityManager containing all activities
     * @param enablePrint     whether output should be printed to the UI
     */
    public void execute(ActivityManager activityManager, boolean enablePrint) {
        UI ui = new UI(enablePrint);

        // Maps lowercase strings to their correctcase equivalents (take the most recent name if there is conflict)
        HashMap<String, String> names = new HashMap<>();
        HashMap<String, Double> netOwedMap = getNetOwedMap(activityManager, names);

        ArrayList<Person> persons = new ArrayList<>();
        for (Map.Entry<String, Double> entry : netOwedMap.entrySet()) {
            if (entry.getValue() == 0.0) {
                continue;
            }
            // Retrieve the correctcase name from names using lowercase name
            String name = names.get(entry.getKey());
            persons.add(new Person(name, entry.getValue(), false));
        }

        ArrayList<String> transactions = new ArrayList<>();

        while (true) {
            if (persons.isEmpty()) {
                break;
            }
            int maxCreditorIndex = getMaxCreditorIndex(persons);
            int maxDebtorIndex = getMaxDebtorIndex(persons);

            if (checkIfBalanceIsEmpty(persons.get(maxCreditorIndex))) {
                break;
            }

            double amountToSettle = Math.min(
                    persons.get(maxCreditorIndex).getAmount(),
                    Math.abs(persons.get(maxDebtorIndex).getAmount()));

            persons.get(maxCreditorIndex).addAmount(-amountToSettle);
            persons.get(maxDebtorIndex).addAmount(amountToSettle);

            String transaction = persons.get(maxDebtorIndex).getName() + " pays " +
                    persons.get(maxCreditorIndex).getName() + " $" + String.format("%.2f", amountToSettle);
            transactions.add(transaction);
        }

        ui.print("Best way to settle debts:");
        for (String t : transactions) {
            ui.print(t);
        }
    }

    /**
     * Checks whether a given person's balance is effectively zero.
     *
     * @param person the person to check
     * @return true if the amount is close to zero; false otherwise
     */
    public boolean checkIfBalanceIsEmpty(Person person) {
        double threshold = 1e-9;
        return Math.abs(person.getAmount()) < threshold;
    }

    /**
     * Finds the index of the person with the maximum positive balance (creditor).
     *
     * @param persons the list of persons
     * @return the index of the maximum creditor
     */
    public int getMaxCreditorIndex(ArrayList<Person> persons) {
        int maxIndex = 0;
        for (int i = 1; i < persons.size(); i++) {
            if (persons.get(i).getAmount() > persons.get(maxIndex).getAmount()) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * Finds the index of the person with the maximum negative balance (debtor).
     *
     * @param persons the list of persons
     * @return the index of the maximum debtor
     */
    public int getMaxDebtorIndex(ArrayList<Person> persons) {
        int minIndex = 0;
        for (int i = 1; i < persons.size(); i++) {
            if (persons.get(i).getAmount() < persons.get(minIndex).getAmount()) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Computes the net owed amount for each person by aggregating all unpaid balances.
     *
     * @param activityManager   the manager containing all activities
     * @param names             a map containing payer and friend names with their lowercase equivalents
     * @return                  a map of each person's name to their net owed amount
     */
    public HashMap<String, Double> getNetOwedMap(ActivityManager activityManager, HashMap<String, String> names) {
        HashMap<String, Double> netOwedMap = new HashMap<>();
        int activitiesSize = activityManager.getSize();
        for (int i = 0; i < activitiesSize; i++) {
            Activity activity = activityManager.getActivity(i);
            addAmountToNetOwedMap(activity, netOwedMap, names);
        }
        return netOwedMap;
    }

    /**
     * Updates the net owed map based on a single activity's unpaid debts.
     *
     * @param activity     the activity to process
     * @param names             a map containing payer and friend names with their lowercase equivalents
     * @param netOwedMap   the map storing cumulative net owed balances
     */
    public void addAmountToNetOwedMap(Activity activity, HashMap<String, Double> netOwedMap,
                                      HashMap<String, String> names) {
        String payerName = activity.getPayer().getName();
        String lowercasePayerName = payerName.toLowerCase();
        Collection<Person> allFriends = activity.getAllFriends();

        names.put(lowercasePayerName, payerName);

        for (Person friend : allFriends) {
            if (friend.hasPaid()) {
                continue;
            }

            String friendName = friend.getName();
            String lowercaseFriendName = friendName.toLowerCase();
            double amountOwed = friend.getAmount();

            names.put(lowercaseFriendName, friendName);

            netOwedMap.put(lowercaseFriendName, netOwedMap.getOrDefault(lowercaseFriendName, 0.0) - amountOwed);
            netOwedMap.put(lowercasePayerName, netOwedMap.getOrDefault(lowercasePayerName, 0.0) + amountOwed);
        }
    }
}
