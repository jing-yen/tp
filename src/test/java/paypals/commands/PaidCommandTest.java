package paypals.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PaidCommandTest extends PayPalsTest {

    @BeforeEach
    public void setUpPaid() {
        callCommand(new AddCommand("d/lunch n/John f/Jane a/28.0"));
        callCommand(new AddCommand("d/tickets n/John f/Betty a/23.53 f/Jane a/20.21 f/Bob a/38.10"));
    }

    @Test
    public void markOnePersonAsPaid_oneFriendMarkedAsUnpaid_personMarkedAsPaid() {
        callCommand(new PaidCommand("n/Jane i/1"));
        Person jane = activityManager.getActivity(0).getFriend("Jane");
        assertTrue(jane.hasPaid(), "Jane should have been marked as unpaid");
    }

    @Test
    public void testOutOfBoundsIdentifier_allUnpaid_throwsException(){
        try {
            callCommand(new PaidCommand("paid n/Jane i/3"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }
    }

    @Test
    public void testInvalidFormat_invalidCommand_throwsException(){
        try {
            callCommand(new PaidCommand("invalid"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.INVALID_FORMAT);
        }
    }

    @Test
    public void testMarkAllAsPaid_everyoneMarkedAsUnpaid_everyoneMarkedAsPaid() {
        callCommand(new PaidCommand("n/John i/2"));
        Collection<Person> friends = activityManager.getActivity(1).getAllFriends();
        for (Person friend : friends) {
            assertTrue(friend.hasPaid(), friend.getName()
                    + " should have been marked as paid");
        }
    }
}
