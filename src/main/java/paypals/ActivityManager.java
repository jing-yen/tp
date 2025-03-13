package paypals;

import java.util.ArrayList;
import java.util.Comparator;

public class ActivityManager {
    private ArrayList<Activities> activitiesArray = new ArrayList<>();
    private ArrayList<Person> netOwed =  new ArrayList<>();
    public void executeAddCommand(String command){
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
