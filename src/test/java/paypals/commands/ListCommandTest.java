package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.Activity;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

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
    public void execute_listActivitiesByPayerWithUppercaseParameters_success() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("N/Alice");
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
        assertEquals("INPUT ERROR: Correct format should be: \n" +
                "list\n" +
                "list n/NAME\n" +
                "list balance n/NAME", e.getMessage());
    }

    @Test
    public void execute_nonExistentName_exceptionThrown() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("n/NotAName");
        try {
            command.execute(manager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.PAYER_NAME_DOES_NOT_EXIST.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_balanceCommand_success() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("balance n/Alice");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void execute_balanceCommandWithUppercaseParameters_success() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("BALANCE N/Alice");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void executeBalanceCommand_negativeBalance_worksCorrectly() {
        ActivityManager manager = new ActivityManager();
        HashMap<String, Double> owedMap = new HashMap<>();
        owedMap.put("Alice", 50.0);
        manager.addActivity(new Activity("Movie", new Person("Bob", -50.0, false), owedMap));
        ListCommand command = new ListCommand("balance n/Alice");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void listActivities_emptyActivityList_showsNoActivities() {
        ActivityManager manager = new ActivityManager();
        ListCommand command = new ListCommand("");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void listActivitiesByFriend_partialPaidState_worksCorrectly() {
        ActivityManager manager = new ActivityManager();
        Activity activity = createTestActivity();
        activity.getFriend("Bob").markAsPaid();
        manager.addActivity(activity);

        ListCommand command = new ListCommand("n/Bob");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void balanceCommand_friendPartiallyPaid_balanceIsAccurate() {
        ActivityManager manager = new ActivityManager();
        Activity activity = createTestActivity();
        activity.getFriend("Bob").markAsPaid(); // Bob already paid
        manager.addActivity(activity);

        ListCommand command = new ListCommand("balance n/Alice");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }

    @Test
    public void isExit_someInput_expectFalse() {
        ActivityManager manager = new ActivityManager();
        ListCommand command = new ListCommand("");

        assertFalse(command.isExit(), "isExit() should return false for a ListCommand");
    }

    //create a sample activity for testing purposes above
    private Activity createTestActivity() {
        HashMap<String, Double> owedMap = new HashMap<>();
        owedMap.put("Bob", 10.0);
        owedMap.put("Charlie", 20.0);
        return new Activity("Dinner", new Person("Alice", -30.0, false), owedMap);
    }

    @Test
    public void listActivitiesByPayer_payerNameWithSpacing_success() {
        ActivityManager manager = new ActivityManager();
        manager.addActivity(createTestActivity());
        ListCommand command = new ListCommand("n/   Alice  ");
        assertDoesNotThrow(() -> command.execute(manager, false));
    }
}
