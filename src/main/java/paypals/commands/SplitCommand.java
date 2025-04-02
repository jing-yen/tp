package paypals.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.Person;
import paypals.util.UI;

public class SplitCommand extends Command {
    String command;

    public SplitCommand(String command) {
        super(command);
    }

    public void execute(ActivityManager activityManager, boolean enablePrint) {
        UI ui = new UI(enablePrint);
        HashMap<String, Double> netOwedMap = getNetOwedMap(activityManager);
        ArrayList<Person> persons = new ArrayList<>();
        for (Map.Entry<String, Double> entry : netOwedMap.entrySet()) {
            if (entry.getValue() == 0.0){
                continue;
            }
            persons.add(new Person(entry.getKey(), entry.getValue(),false));
        }
        ArrayList<String> transactions = new ArrayList<>();

        while (true) {
            if (persons.isEmpty()) {
                break;
            }
            int maxCreditorIndex = getMaxCreditorIndex(persons);
            int maxDebtorIndex = getMaxDebtorIndex(persons);

            // Terminate when the maximum creditor's balance is effectively zero.
            // Since the total sum is zero, if the max creditor is near zero, all balances are settled.
            if (checkIfBalanceIsEmpty(persons.get(maxCreditorIndex))) {
                break;
            }
            // Determine the amount to settle.
            double amountToSettle = Math.min(persons.get(maxCreditorIndex).getAmount(),
                    Math.abs(persons.get(maxDebtorIndex).getAmount()));

            // Update the balances.
            persons.get(maxCreditorIndex).addAmount(-amountToSettle);
            persons.get(maxDebtorIndex).addAmount(amountToSettle);

            // Save the transaction.
            String transaction = persons.get(maxDebtorIndex).getName() + " pays " +
                    persons.get(maxCreditorIndex).getName() + " $" + String.format("%.2f", amountToSettle);
            transactions.add(transaction);
        }

        // Print out each saved transaction one by one.
        ui.print("Best way to settle debts:");
        for (String t : transactions) {
            ui.print(t);
        }
    }

    public boolean checkIfBalanceIsEmpty(Person person) {
        double threshold = 1e-9;
        return Math.abs(person.getAmount()) < threshold;
    }

    public int getMaxCreditorIndex(ArrayList<Person> persons) {
        int maxIndex = 0;
        for (int i = 1; i < persons.size(); i++) {
            if (persons.get(i).getAmount() > persons.get(maxIndex).getAmount()) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public int getMaxDebtorIndex(ArrayList<Person> persons) {
        int minIndex = 0;
        for (int i = 1; i < persons.size(); i++) {
            if (persons.get(i).getAmount() < persons.get(minIndex).getAmount()) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    public HashMap<String, Double> getNetOwedMap(ActivityManager activityManager) {
        HashMap<String, Double> netOwedMap = new HashMap<>();
        int activitiesSize = activityManager.getSize();
        for (int i = 0; i < activitiesSize; i++) {
            Activity activity = activityManager.getActivity(i);
            addAmountToNetOwedMap(activity,netOwedMap);
        }
        return netOwedMap;
    }

    public void addAmountToNetOwedMap(Activity activity, HashMap<String, Double> netOwedMap){
        String payerName = activity.getPayer().getName();
        Collection<Person> allFriends = activity.getAllFriends();
        for (Person friend : allFriends) {
            if (friend.hasPaid()){
                continue;
            }
            String friendName = friend.getName();
            Double amountOwed = friend.getAmount();
            netOwedMap.put(friendName, netOwedMap.getOrDefault(friendName,0.0) - amountOwed);
            netOwedMap.put(payerName, netOwedMap.getOrDefault(payerName,0.0) + amountOwed);
        }
    }
}
