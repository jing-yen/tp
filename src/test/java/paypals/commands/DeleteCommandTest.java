package paypals.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DeleteCommandTest extends PayPalsTest {

    HashMap<String, Double> netOwedMap = activityManager.getNetOwedMap();

    @BeforeEach
    public void setUpDelete() throws PayPalsException {
        AddCommand c1 = new AddCommand("d/lunch n/john f/jane a/30 f/jake a/20");
        c1.execute(activityManager, false);
        AddCommand c2 = new AddCommand("d/dinner n/jane f/john a/20 f/jake a/20");
        c2.execute(activityManager, false);
        for (String key : netOwedMap.keySet()) {  //ensure set up is correct
            if (key.equals("john")) {
                assertEquals(30.0, netOwedMap.get(key), 0.001, "For john expected 30.0");
            } else if (key.equals("jane")) {
                assertEquals(10.0, netOwedMap.get(key), 0.001, "For jane expected 10.0");
            } else if (key.equals("jake")) {
                assertEquals(-40.0, netOwedMap.get(key), 0.001, "For jake expected -40.0");
            } else {
                fail("Unexpected key in netOwedMap: " + key);
            }
        }
    }

    @Test
    public void execute_validIdentifier_correctlyUpdatesNetOwedMap() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/2");
        command.execute(activityManager, false);
        for (String key : netOwedMap.keySet()) {  //ensure set up is correct
            if (key.equals("john")) {
                assertEquals(50.0, netOwedMap.get(key), 0.001, "For john expected 50.0");
            } else if (key.equals("jane")) {
                assertEquals(-30.0, netOwedMap.get(key), 0.001, "For jane expected -30.0");
            } else if (key.equals("jake")) {
                assertEquals(-20.0, netOwedMap.get(key), 0.001, "For jake expected -20.0");
            } else {
                fail("Unexpected key in netOwedMap: " + key);
            }
        }
    }

    @Test
    public void execute_deleteAllActivities_netOwedMapIsEmpty() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/1");
        command.execute(activityManager, false);
        DeleteCommand command2 = new DeleteCommand("i/1");
        command2.execute(activityManager, false);
        assertTrue(netOwedMap.isEmpty(), "Expected empty netOwedMap");
    }

    @Test
    public void execute_outOfBoundsIdentifier_exceptionThrown() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/5");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }
    }

    @Test
    public void execute_noIdentifier_exceptionThrown() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.NO_IDENTIFIER);
        }
    }

    @Test
    public void execute_invalidIdentifier_exceptionThrown() throws PayPalsException {
        DeleteCommand command = new DeleteCommand("i/999999999999999999999999");
        try {
            command.execute(activityManager, false);
            fail("Expected PayPalsException but none was thrown");
        } catch (PayPalsException e) {
            assertException(e, ExceptionMessage.INVALID_IDENTIFIER);
        }
    }


}
