package paypals;

import paypals.commands.AddCommand;
import paypals.commands.Command;
import paypals.commands.DeleteCommand;
import paypals.commands.EditCommand;
import paypals.commands.PaidCommand;
import paypals.commands.UnpaidCommand;
import paypals.exception.PayPalsException;
import paypals.util.Group;
import paypals.util.Parser;
import paypals.util.Storage;
import paypals.util.UI;
import paypals.util.Logging;

public class PayPals {
    private static Parser parser;
    private static ActivityManager activityManager;
    private static Storage storage;
    private static UI ui;
    private static Group group;

    public PayPals() {
        try {
            new Logging();
            ui = new UI(true);
            parser = new Parser();
            activityManager = new ActivityManager();
            storage = new Storage();
            new Group(ui, storage, activityManager);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        boolean isExit = false;
        Logging.logInfo("Entering main program body. Begin accepting user commands");
        ui.sayHello();
        ui.printLine();
        Group.groupSelection();
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
