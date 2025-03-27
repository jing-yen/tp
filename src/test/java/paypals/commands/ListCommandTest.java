package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.Activity;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.PayPalsException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest extends PayPalsTest {

    @Test
    public void execute_listAllActivities_success() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void execute_listActivitiesByPayer_success() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("n/Alice");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void execute_listActivitiesByFriend_success() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("n/Bob");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void execute_invalidFormat_exceptionThrown() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("n=Bob");
        PayPalsException e = assertThrows(PayPalsException.class, () -> command.execute(manager, true));
        assertEquals("INPUT ERROR: Correct format should be: list n/NAME", e.getMessage());
    }

    @Test
    public void execute_nonExistentName_exceptionThrown() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("n/NotAName");
        PayPalsException e = assertThrows(PayPalsException.class, () -> command.execute(manager, true));
        assertEquals("INPUT ERROR: No name of payer", e.getMessage());
    }

    //create a sample activity for testing purposes above
    private Activity createTestActivity() {
        HashMap<String, Double> owedMap = new HashMap<>();
        owedMap.put("Bob", 10.0);
        owedMap.put("Charlie", 20.0);
        return new Activity("Dinner", new Person("Alice", -30.0, false), owedMap);
    }
}
