package paypals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paypals.commands.Command;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.HashMap;

public class PayPalsTest {

    protected ActivityManager activityManager;
    protected HashMap<String, Double> netOwedMap;

    @BeforeEach
    public void setUp() {
        activityManager = new ActivityManager();
        netOwedMap = activityManager.getNetOwedMap();
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

    @Test
    public void sampleTest() {
        assertTrue(true);
    }
}
