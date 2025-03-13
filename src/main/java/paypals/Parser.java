package paypals;

import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class Parser {
    public void decodeCommand(ActivityManager activityManager, String command) {
        try {
            String[] tokens = command.split(" ", 2);
            switch (tokens[0]) {
            case "add":
                activityManager.executeAddCommand(tokens[1]);
                break;
            case "delete":
                activityManager.executeDeleteCommand(Integer.parseInt(tokens[1]));
                break;
            case "list":
                activityManager.executeListCommand(tokens[1]);
                break;
            case "split":
                activityManager.executeSplitCommand();
                break;
            default:
                throw new PayPalsException(ExceptionMessage.INVALID_COMMAND);
            }
        } catch (PayPalsException e){
            System.out.println(e.getMessage());
        }

    }

}
