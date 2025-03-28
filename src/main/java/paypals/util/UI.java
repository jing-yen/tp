package paypals.util;

import java.util.Scanner;

public class UI {
    private final Scanner in;
    private boolean enablePrint;

    public UI(boolean enablePrint) {
        this.in = new Scanner(System.in);
        this.enablePrint = enablePrint;
    }

    public String readLine() {
        return in.nextLine();
    }

    public void print(String message) {
        if (enablePrint) {
            System.out.println(message);
        }
    }

    public void printPrompt() {
        System.out.print("> ");
    }

    public void printLine() {
        System.out.println("____________________________________________________________");
    }

    public void sayHello(){
        print("\n\nWelcome to PayPals!");
    }

    public void sayGoodbye() {
        print("Thank you for using PayPals!");
        print("Hope you have enjoyed your trip and see you again soon!");
    }
}
