package paypals.util;

import java.util.ArrayList;
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

    //@@author jing-yen
    public void printGroupNames(ArrayList<String> groupNames) {
        int index = 0;
        while (index < groupNames.size()) {
            print(String.format("(%d) %s", index+1, groupNames.get(index++)));
        }
        if (groupNames.isEmpty()) {
            print("(You currently have no available groups to load)");
        }
    }

}
