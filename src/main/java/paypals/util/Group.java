package paypals.util;

import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;

public class Group {
    private static UI ui;
    private static Storage storage;
    private static ActivityManager activityManager;


    public Group(UI ui,Storage storage, ActivityManager activityManager) {
        Group.ui = ui;
        Group.storage = storage;
        Group.activityManager = activityManager;

    }

    public static boolean isExit() {
        return true;
    }

    public static void groupSelection() {
        ui.sayHello();
        ui.printLine();
        boolean validCommand = false;
        while (!validCommand) {
            ui.print("Would you like to delete or select a group?");
            ui.printLine();
            ui.printPrompt();
            String command = ui.readLine();
            if (command.equals("select")) {
                validCommand = true;
                selectGroup();
            } else if (command.equals("delete")) {
                try {
                    deleteGroup();
                    validCommand = true;
                } catch (PayPalsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                ui.print("Please enter select or delete.");
            }
        }
    }

    private static void deleteGroup() throws PayPalsException {
        ArrayList<String> groupNames = storage.getGroupNames();
        if (groupNames.isEmpty()) {
            throw new PayPalsException(ExceptionMessage.NO_GROUP);
        }
        ui.print("Please select a group number from the following:");
        ui.printGroupNames(groupNames);

        String groupNumberOrName = "";
        boolean validInput = false;
        while (!validInput) {
            try {
                ui.printPrompt();
                groupNumberOrName = ui.readLine();
                validInput = storage.containsFile(groupNumberOrName);
            } catch (PayPalsException e) {
                System.out.println(e.getMessage());
            }
        }
        storage.delete(groupNumberOrName, activityManager);
        groupSelection();
    }

    //@@author jing-yen
    private static void selectGroup() {
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

