package paypals.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paypals.PayPalsTest;
import paypals.Person;
import paypals.exception.ExceptionMessage;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PaidCommandTest extends PayPalsTest {

    @BeforeEach
    public void setUpPaid() {
        callCommand(new AddCommand("d/travel n/Alice f/John a/50 f/Jane a/50 f/Bob a/50"));
    }

    @Test
    public void execute_payerIsNotFriend_marksOnlyOnePerson() {
        callCommand(new PaidCommand("n/Jane i/1"));
        Person jane = activityManager.getActivity(0).getFriend("Jane");
        Person john = activityManager.getActivity(0).getFriend("John");
        assertTrue(jane.hasPaid(), "Jane should be marked as paid");
        assertFalse(john.hasPaid(), "John should not be marked as paid");
    }

    @Test
    public void someFriendsAlreadyPaid_payerMarksAll_remainingMarkedAsPaid() {
        activityManager.getActivity(0).getFriend("Jane").markAsPaid(); // Jane already paid
        callCommand(new PaidCommand("n/Alice i/1")); // payer pays for everyone else

        Collection<Person> friends = activityManager.getActivity(0).getAllFriends();
        for (Person friend : friends) {
            assertTrue(friend.hasPaid(), friend.getName() + " should have been marked as paid");
        }
    }

    @Test
    public void execute_missingWhitespaceBetweenArguments_throwsInvalidFormat() {
        try {
            callCommand(new PaidCommand("n/Johni/1"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.INVALID_FORMAT);
        }
    }

    @Test
    public void execute_extraArgumentsAfterIndex_throwsInvalidFormat() {
        try {
            callCommand(new PaidCommand("n/John i/1 extra"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.INVALID_FORMAT);
        }
    }

    @Test
    public void execute_friendNotInActivity_throwsInvalidFriend() {
        try {
            callCommand(new PaidCommand("n/Charlie i/1"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.INVALID_FRIEND);
        }
    }

    @Test
    public void execute_friendHasNoUnpaidActivities_throwsOutOfBoundsIdentifier() {
        for (Person friend : activityManager.getActivity(0).getAllFriends()) {
            friend.markAsPaid();
        }

        try {
            callCommand(new PaidCommand("n/Bob i/1"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }
    }

    @Test
    public void execute_negativeActivityIndex_throwsOutOfBoundsIdentifier() {
        try {
            callCommand(new PaidCommand("n/John i/-1"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }
    }

    @Test
    public void execute_zeroActivityIndex_throwsOutOfBoundsIdentifier() {
        try {
            callCommand(new PaidCommand("n/John i/0"));
        } catch (Exception e) {
            assertException(e, ExceptionMessage.OUTOFBOUNDS_IDENTIFIER);
        }
    }

}
