package paypals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paypals.exception.ExceptionMessage;

public class PayPalsTest {

    protected ActivityManager activityManager;

    @BeforeEach
    public void setUp() {
        activityManager = new ActivityManager();
    }

    protected void assertException(Exception e, ExceptionMessage em) {
        assertTrue(e.getMessage().contains(em.getMessage()));
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }
}
