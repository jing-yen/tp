package paypals;

import java.util.Scanner;

import paypals.commands.Command;
import paypals.exception.PayPalsException;

public class PayPals {
    private static Parser parser;
    private static ActivityManager activityManager;

    public PayPals() {
        parser = new Parser();
        activityManager = new ActivityManager();
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = in.nextLine();
                Command c = parser.decodeCommand(fullCommand);
                c.execute(activityManager);
                isExit = c.isExit();
            } catch (PayPalsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new PayPals().run();
    }
}
