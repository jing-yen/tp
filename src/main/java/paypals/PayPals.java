package paypals;

import java.util.Scanner;

import paypals.commands.Command;
import paypals.exception.PayPalsException;
import paypals.util.Logging;
import paypals.util.Parser;
import paypals.util.Storage;

public class PayPals {
    private static Parser parser;
    private static ActivityManager activityManager;
    private static Storage storage;

    public PayPals() {
        try {
            new Logging();
            parser = new Parser();
            activityManager = new ActivityManager();
            storage = new Storage();
            storage.load(activityManager);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        boolean isExit = false;
        Logging.logInfo("Entering main program body. Begin accepting user commands");
        while (!isExit) {
            try {
                System.out.print("> ");
                String fullCommand = in.nextLine();
                Command c = parser.decodeCommand(fullCommand);
                c.execute(activityManager);
                isExit = c.isExit();
            } catch (PayPalsException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            storage.save(activityManager);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to PayPals!");
        new PayPals().run();
    }
}
