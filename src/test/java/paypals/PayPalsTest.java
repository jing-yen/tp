package paypals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import paypals.commands.Command;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

public class PayPalsTest {

    protected ActivityManager activityManager;

    @BeforeEach
    public void setUp() {
        activityManager = new ActivityManager();
    }

    protected void assertException(Exception e, ExceptionMessage em) {
        assertTrue(e.getMessage().contains(em.getMessage()));
    }

    protected void callCommand(Command command) {
        try {
            command.execute(activityManager, true);
        } catch (PayPalsException e) {
            System.out.println(e.getMessage());
        }
    }
}
