package paypals.commands;

import java.util.ArrayList;
import java.util.Comparator;

import paypals.ActivityManager;
import paypals.Person;

public class SplitCommand extends Command {
    String command;

    public SplitCommand(String command) {
        super(command);
    }

    public void execute(ActivityManager activityManager) {
        ArrayList<Person> netOwed = activityManager.getNetOwed();

        netOwed.sort(Comparator.comparingDouble(Person::getAmount).reversed());

        System.out.println("Best way to settle debts:");

        int leftIndex = 0;
        int rightIndex = netOwed.size() - 1;

        while (leftIndex < rightIndex) {
            Person leftPerson = netOwed.get(leftIndex);  // Creditor
            Person rightPerson = netOwed.get(rightIndex); // Debtor

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
