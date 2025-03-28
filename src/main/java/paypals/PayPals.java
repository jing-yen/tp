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
            storage.load(activityManager);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        ui.sayHello();
        boolean isExit = false;
        Logging.logInfo("Entering main program body. Begin accepting user commands");
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
        ui.printLine();
        ui.print("");
    }

    public static void main(String[] args) {
        new PayPals().run();
    }

}
