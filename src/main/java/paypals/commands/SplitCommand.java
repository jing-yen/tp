package paypals.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        HashMap<String, Double> netOwedMap = activityManager.getNetOwedMap();
        ArrayList<Person> persons = new ArrayList<>();
        for (Map.Entry<String, Double> entry : netOwedMap.entrySet()) {
            persons.add(new Person(entry.getKey(), entry.getValue(),false));
        }
        ArrayList<String> transactions = new ArrayList<>();

        while (true) {
            int maxCreditorIndex = getMaxCreditorIndex(persons);
            int maxDebtorIndex = getMaxDebtorIndex(persons);

            // Terminate when the maximum creditor's balance is effectively zero.
            // Since the total sum is zero, if the max creditor is near zero, all balances are settled.
            if (Math.abs(persons.get(maxCreditorIndex).getAmount()) < 1e-9) {
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
}



/*public void execute(ActivityManager activityManager) {
    HashMap<String,Double> netOwedMap = activityManager.getNetOwedMap();
    ArrayList<Person> netOwedArray = new ArrayList<>();
    for (Map.Entry<String, Double> entry : netOwedMap.entrySet()) {
        String key = entry.getKey();
        Double value = entry.getValue();
        netOwedArray.add(new Person(key,value,false));
    }
    netOwedArray.sort(Comparator.comparingDouble(Person::getAmount).reversed());

    System.out.println("Best way to settle debts:");

    int leftIndex = 0;
    int rightIndex = netOwedArray.size() - 1;

    while (leftIndex < rightIndex) {
        Person leftPerson = netOwedArray.get(leftIndex);  // Creditor
        Person rightPerson = netOwedArray.get(rightIndex); // Debtor

        double leftAmount = leftPerson.getAmount();
        double rightAmount = Math.abs(rightPerson.getAmount());

        if (leftAmount > rightAmount) { // Creditor has more than debtor can pay
            leftPerson.addAmount(leftAmount - rightAmount);
            rightPerson.addAmount(0);
            System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + rightAmount);
            rightIndex--; // Move to next debtor
        } else if (leftAmount < rightAmount) { // Debtor still owes after paying
            rightPerson.addAmount(-(rightAmount - leftAmount)); // Keep it negative
            leftPerson.addAmount(0);
            System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + leftAmount);
            leftIndex++; // Move to next creditor
        } else { // Exact match
            rightPerson.addAmount(0);
            leftPerson.addAmount(0);
            System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + rightAmount);
            leftIndex++;
            rightIndex--;
            if (leftAmount > rightAmount) { // Creditor has more than debtor can pay
                leftPerson.addAmount(leftAmount - rightAmount);
                rightPerson.addAmount(0);
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + rightAmount);
                rightIndex--; // Move to next debtor
            } else if (leftAmount < rightAmount) { // Debtor still owes after paying
                rightPerson.addAmount(-(rightAmount - leftAmount)); // Keep it negative
                leftPerson.addAmount(0);
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + leftAmount);
                leftIndex++; // Move to next creditor
            } else { // Exact match
                rightPerson.addAmount(0);
                leftPerson.addAmount(0);
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + rightAmount);
                leftIndex++;
                rightIndex--;
            }
        }
    }
}*/
