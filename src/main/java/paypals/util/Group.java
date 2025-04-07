package paypals.util;

import paypals.ActivityManager;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.ArrayList;

/**
 * The Group class manages operations related to groups in the PayPals application.
 * It provides functionality for selecting, creating, and deleting groups, and
 * interfaces with the Storage and ActivityManager components.
 */
public class Group {
    private static UI ui;
    private static Storage storage;
    private static ActivityManager activityManager;

    /**
     * Constructs a new Group instance with the specified components.
     *
     * @param ui The user interface component for handling user interaction
     * @param storage The storage component for file operations
     * @param activityManager The activity manager for expense tracking
     */
    public Group(UI ui, Storage storage, ActivityManager activityManager) {
        Group.ui = ui;
        Group.storage = storage;
        Group.activityManager = activityManager;
    }

    /**
     * The method displays a welcome message and prompts the user to choose
     * between selecting or deleting a group.
     */
    public static void groupSelection() {
        ui.sayHello();
        ui.printLine();
        boolean groupSelected = false;

        while (!groupSelected) {
            ui.print("Would you like to delete or select a group?");
            ui.printLine();
            ui.printPrompt();
            String command = ui.readLine().toLowerCase();
            if (command.equals("select")) {
                groupSelected = true;
                selectGroup();
            } else if (command.equals("delete")) {
                try {
                    deleteGroup();
                } catch (PayPalsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                ui.print("Please enter select or delete.");
            }
        }
    }

    /**
     * Handles the deletion of an existing group.
     * This method displays available groups, prompts the user to select a group to delete,
     * and then removes the selected group from storage.
     *
     * @throws PayPalsException If there are no groups available for deletion or if deletion fails
     */
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
        ui.print("Group has been deleted.");
        ui.printLine();
    }

    /**
     * Handles the selection or creation of a group.
     * This method displays existing groups, allows the user to select an existing group
     * or create a new one by providing a name, and loads the selected group's data.
     */
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

