package paypals;

import paypals.exception.PayPalsException;
import paypals.util.Storage;
import paypals.util.UI;

import java.util.ArrayList;

public class Group {
    private static UI ui;
    private static Storage storage;
    private static ActivityManager activityManager;

    Group(Storage storage, ActivityManager activityManager) {
        ui = new UI(true);
        Group.storage = storage;
        Group.activityManager = activityManager;
    }

    //@@author jing-yen
    public static void groupSelection(){
        ui.sayHello();
        ui.printLine();

        ui.print("Please select a group number from the following:");
        ArrayList<String> groupNames = storage.getGroupNames();
        ui.printGroupNames(groupNames);
        ui.print("or give your new group a name:");

        String groupNumberOrName = "";
        boolean validInput = false;
        while (!validInput) {
            try {
                ui.printPrompt();
                groupNumberOrName = ui.readLine();
                validInput = storage.checkIfFilenameValid(groupNumberOrName);
            } catch (PayPalsException e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            storage.load(groupNumberOrName, activityManager);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }

        ui.printLine();
        ui.print(String.format("You are currently in the \"%s\" group.", activityManager.getGroupName()));
        if (activityManager.checkIsNewGroup()) {
            ui.print("It is a new group.");
        } else {
            ui.print(String.format("There are %d transactions.", activityManager.getActivityList().size()));
        }
    }
}
