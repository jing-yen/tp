package paypals;

import paypals.commands.AddCommand;
import paypals.commands.Command;
import paypals.commands.DeleteCommand;
import paypals.commands.EditCommand;
import paypals.commands.PaidCommand;
import paypals.commands.UnpaidCommand;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.Parser;
import paypals.util.Storage;
import paypals.util.UI;

import java.util.ArrayList;

public class PayPals {
    private static Parser parser;
    private static ActivityManager activityManager;
    private static Storage storage;
    private static UI ui;

    public PayPals() {
        try {
            new Logging();
            ui = new UI(true);
            parser = new Parser();
            activityManager = new ActivityManager();
            storage = new Storage();
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        boolean isExit = false;
        Logging.logInfo("Entering main program body. Begin accepting user commands");
        ui.sayHello();
        ui.printLine();

        System.out.println("Please select a group number...");
        ArrayList<String> groupNames = storage.getGroupNames();
        int index = 0;
        while (index < groupNames.size()) {
            System.out.println(String.format("(%d) %s", index+1, groupNames.get(index++)));
        }
        System.out.println("... or give your new group a name:");

        ui.printPrompt();
        String groupNumberOrName = ui.readLine();
        while (!storage.checkIfFilenameValid(groupNumberOrName)) {
            ui.print("Group name needs to be a valid filename as well. Try again:");
            ui.printPrompt();
            groupNumberOrName = ui.readLine();
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

        while (!isExit) {
            try {
                ui.printLine();
                ui.printPrompt();
                String fullCommand = ui.readLine();
                ui.printLine();
                Command c = parser.decodeCommand(fullCommand);
                c.execute(activityManager, true);
                isExit = c.isExit();
                if (c instanceof AddCommand || c instanceof EditCommand || c instanceof DeleteCommand
                        || c instanceof PaidCommand || c instanceof UnpaidCommand) {
                    storage.save(activityManager);
                }
            } catch (PayPalsException e) {
                System.out.println(e.getMessage());
            }
        }
        ui.sayGoodbye();
    }

    public static void main(String[] args) {
        new PayPals().run();
    }

}
