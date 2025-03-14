package paypals.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import paypals.ActivityManager;
import paypals.Person;

public class SplitCommand extends Command {
    String command;

    public SplitCommand(String command) {
        super(command);
    }

    public void execute(ActivityManager activityManager) {
        HashMap<String,Double> netOwedMap = activityManager.getNetOwedMap();
        ArrayList<Person> netOwedArray = new ArrayList<>();
        for (Map.Entry<String, Double> entry : netOwedMap.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            netOwedArray.add(new Person(key,value));
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
                leftPerson.setAmount(leftAmount - rightAmount);
                rightPerson.setAmount(0);
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + rightAmount);
                rightIndex--; // Move to next debtor
            } else if (leftAmount < rightAmount) { // Debtor still owes after paying
                rightPerson.setAmount(-(rightAmount - leftAmount)); // Keep it negative
                leftPerson.setAmount(0);
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + leftAmount);
                leftIndex++; // Move to next creditor
            } else { // Exact match
                rightPerson.setAmount(0);
                leftPerson.setAmount(0);
                System.out.println(rightPerson.getName() + " pays " + leftPerson.getName() + " $" + rightAmount);
                leftIndex++;
                rightIndex--;
            }
        }
    }
}
