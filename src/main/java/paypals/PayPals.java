package paypals;

import java.util.Scanner;

import paypals.commands.Command;
import paypals.exception.PayPalsException;

public class PayPals {
    private static Parser parser;
    private static ActivityManager activityManager;
    public static boolean isExit = false;

    public PayPals() {
        parser = new Parser();
        activityManager = new ActivityManager();
    }

    public static void main(String[] args) {
        PayPals payPals = new PayPals();
        Scanner in = new Scanner(System.in);
        while (!isExit) {
            try {
                String fullCommand = in.nextLine();
                Command c = parser.decodeCommand(fullCommand);
                c.execute(activityManager);
            } catch (PayPalsException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
