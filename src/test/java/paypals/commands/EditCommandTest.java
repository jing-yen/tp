package paypals.commands;

import paypals.PayPalsTest;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EditCommandTest extends PayPalsTest {

    @BeforeEach
    public void setUpEditTests() throws PayPalsException {
        AddCommand c1 = new AddCommand("add d/lunch n/Jane f/John a/28");
        c1.execute(activityManager, false);
    }

    @Test
    public void execute_emptyString_exceptionThrown() {
        String command = "";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_editDescription_success() {
        // Edit the description of the first activity (index 1, which maps to index 0).
        String command = "i/1 d/NewLunch";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify the description is updated.
        assertEquals("NewLunch", activityManager.getActivity(0).getDescription());
    }

    @Test
    public void execute_editPayer_success() {
        // Edit the payer name of the first activity.
        String command = "i/1 n/NewJane";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that the payer's name is updated.
        assertEquals("NewJane", activityManager.getActivity(0).getPayer().getName());
    }

    @Test
    public void execute_editFriendName_success() {
        // Change the owed friend's name: from "John" to "NewJohn" in the first activity.
        String command = "i/1 f/Johnny o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that "John" is replaced with "NewJohn".
        assertNull(activityManager.getActivity(0).getFriend("John"), "Old friend name should be removed");
        assertNotNull(activityManager.getActivity(0).getFriend("Johnny"), "New friend name should exist");
    }

    @Test
    public void execute_editFriendAmount_success() {
        // Change the amount owed by friend "John" to 10 for the first activity.
        String command = "i/1 a/10 o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that John's owed amount is updated to 35.
        assertEquals(10.0, activityManager.getActivity(0).getFriend("John").getAmount(), 0.001);
    }

    @Test
    public void execute_editFriendAmountWhenPaid_exceptionThrown() {
        // Mark friend "John" as having paid.
        String paidCommand = "n/John i/1";
        PaidCommand pc = new PaidCommand(paidCommand);
        assertDoesNotThrow(() -> pc.execute(activityManager, false));

        String command = "i/1 a/35 o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_AMOUNT_WHEN_PAID.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_invalidParameters_exceptionThrown() {
        String command = "i/1 d/Desc p/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_missingId_throwsException() {
        String command = "d/Desc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NO_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_invalidId_throwsException() {
        String command = "i/test d/Desc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_outOfBoundsId_throwsException() {
        String command = "i/-1 d/Desc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_invalidAmount_throwsException() {
        String command = "i/1 a/test o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_AMOUNT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_negativeAmount_throwsException() {
        String command = "i/1 a/-10 o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.AMOUNT_OUT_OF_BOUNDS.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_largeAmount_throwsException() {
        String command = "i/1 a/999999 o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.LARGE_AMOUNT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void isExit_someInput_expectFalse() {
        String command = "";
        EditCommand ec = new EditCommand(command);

        assertFalse(ec.isExit(), "isExit() should return false for an EditCommand");
    }

    // Test that providing more than one valid edit parameter
    // (e.g. both description and payer) throws an EDIT_FORMAT_ERROR.
    @Test
    public void execute_multipleEdits_exceptionThrown() {
        String command = "i/1 d/NewDesc n/NewPayer";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected EDIT_FORMAT_ERROR exception due to multiple edit parameters");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test editing friend amount with extra tokens in the amount string (e.g., "20 extra")
    // throws an INVALID_AMOUNT exception.
    @Test
    public void execute_editFriendAmountExtraTokensInAmount_exceptionThrown() {
        String command = "i/1 a/20 extra o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected INVALID_AMOUNT exception due to extra tokens in amount");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_AMOUNT.getMessage(), e.getMessage());
        }
    }

    // Test editing friend amount with an amount that has more than two decimals should throw NOT_MONEY_FORMAT.
    @Test
    public void execute_editFriendAmountThreeDecimals_exceptionThrown() {
        String command = "i/1 a/20.555 o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected NOT_MONEY_FORMAT exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NOT_MONEY_FORMAT.getMessage(), e.getMessage());
        }
    }

    // Test editing friend amount with zero value should throw AMOUNT_OUT_OF_BOUNDS.
    @Test
    public void execute_editFriendAmountZero_exceptionThrown() {
        String command = "i/1 a/0 o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected AMOUNT_OUT_OF_BOUNDS exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.AMOUNT_OUT_OF_BOUNDS.getMessage(), e.getMessage());
        }
    }

    // Test editing friend amount with a valid amount exactly equal to the limit (10000) succeeds.
    @Test
    public void execute_editFriendAmountAtLimit_success() {
        String command = "i/1 a/10000 o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify the update. (Assumes that activityManager.getActivity(0).getFriend("John") exists.)
        assertEquals(10000.0, activityManager.getActivity(0).getFriend("John").getAmount(), 0.001);
    }

    // Test editing friend amount with an amount just above the limit throws a LARGE_AMOUNT exception.
    @Test
    public void execute_editFriendAmountAboveLimit_exceptionThrown() {
        String command = "i/1 a/10000.01 o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected LARGE_AMOUNT exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.LARGE_AMOUNT.getMessage(), e.getMessage());
        }
    }

    // Test editing friend name with extra spaces in the new name – they should be trimmed.
    @Test
    public void execute_editFriendNameExtraSpaces_trimmed() {
        String command = "i/1 f/   NewJohn   o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that the old friend ("John") is removed and the new friend ("NewJohn") is present.
        assertNull(activityManager.getActivity(0).getFriend("John"), "Old friend name should be removed");
        assertNotNull(activityManager.getActivity(0).getFriend("NewJohn"), "New friend name should exist");
    }

    // Test editing payer name with extra spaces – they should be trimmed.
    @Test
    public void execute_editPayerExtraSpaces_trimmed() {
        String command = "i/1 n/   NewJane   ";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify the payer's name is trimmed and updated.
        assertEquals("NewJane", activityManager.getActivity(0).getPayer().getName());
    }

    // Test that the order of parameters does not matter (e.g., identifier can come after the edit parameter).
    @Test
    public void execute_editPayerOrderIrrelevant_success() {
        String command = "n/AnotherJane i/1";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        assertEquals("AnotherJane", activityManager.getActivity(0).getPayer().getName());
    }

    // Test when editing a friend's name, if the 'o/' (old friend name)
    // parameter is missing, EDIT_FORMAT_ERROR is thrown.
    @Test
    public void execute_editFriendNameMissingOldName_exceptionThrown() {
        String command = "i/1 f/NewJohn";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected EDIT_FORMAT_ERROR exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test when editing a friend's amount, if the 'o/' (old friend name) parameter is missing,
    // EDIT_FORMAT_ERROR is thrown.
    @Test
    public void execute_editFriendAmountMissingOldName_exceptionThrown() {
        String command = "i/1 a/20";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected EDIT_FORMAT_ERROR exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test when only the identifier is provided (no edit operation), EDIT_FORMAT_ERROR should be thrown.
    @Test
    public void execute_onlyIdentifierProvided_exceptionThrown() {
        String command = "i/1";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected EDIT_FORMAT_ERROR exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test editing friend amount for a non-existent friend should throw an exception.
    // (If your ActivityManager returns null for a non-existent friend, this may result in
    // a NullPointerException or a custom exception.)
    @Test
    public void execute_editFriendAmountNonExistentFriend_exceptionThrown() {
        String command = "i/1 a/20 o/NonExistent";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected exception when editing non-existent friend amount");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.FRIEND_DOES_NOT_EXIST.getMessage(), e.getMessage());
        }
    }

    // Test editing friend name for a non-existent friend should throw an exception.
    @Test
    public void execute_editFriendNameNonExistentFriend_exceptionThrown() {
        String command = "i/1 f/NewName o/NonExistent";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected exception when editing non-existent friend name");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.FRIEND_DOES_NOT_EXIST.getMessage(), e.getMessage());
        }
    }

    // Test that a null command input throws EDIT_FORMAT_ERROR.
    @Test
    public void execute_nullCommand_exceptionThrown() {
        try {
            EditCommand ec = new EditCommand(null);
            ec.execute(activityManager, false);
            fail("Expected EDIT_FORMAT_ERROR exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test that a command containing only whitespace throws EDIT_FORMAT_ERROR.
    @Test
    public void execute_whitespaceOnlyCommand_exceptionThrown() {
        String command = "    ";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected EDIT_FORMAT_ERROR exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test that an unknown parameter (one that doesn’t match [idnfao]) is ignored.
    @Test
    public void execute_unknownParameter_exceptionThrown() {
        String command = "i/1 d/UpdatedDesc x/IgnoreThis";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test that duplicate description parameters result in the last one being used.
    @Test
    public void execute_duplicateDescriptionParameters_lastOneWins() {
        String command = "i/1 d/FirstDesc d/SecondDesc";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // The second occurrence should overwrite the first.
        assertEquals("SecondDesc", activityManager.getActivity(0).getDescription());
    }

    // Test that commands with tabs and newlines as whitespace are parsed correctly.
    @Test
    public void execute_withTabsAndNewlines_success() {
        String command = "i/1\td/UpdatedDesc\n";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        assertEquals("UpdatedDesc", activityManager.getActivity(0).getDescription());
    }

    // Test that leading and trailing spaces in a description are trimmed.
    @Test
    public void execute_descriptionWithExtraSpaces_trimmed() {
        String command = "i/1 d/   TrimmedDesc   ";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        assertEquals("TrimmedDesc", activityManager.getActivity(0).getDescription());
    }

    // Test that a description with special characters is accepted.
    @Test
    public void execute_descriptionWithSpecialCharacters_success() {
        String command = "i/1 d/Desc!@#123";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        assertEquals("Desc!@#123", activityManager.getActivity(0).getDescription());
    }

    // Obscure input: editing a friend’s name where the new name contains a slash.
    // Due to the regex, the value will be cut off at the first slash.
    // Expected behavior: the new friend name will be parsed as only "New" so that the edit operation
    // will likely throw FRIEND_DOES_NOT_EXIST if "Name" was intended.
    // (Adjust this test’s expected behavior if you choose to support slashes.)
    @Test
    public void execute_editFriendNameWithSlashInName_throwsException() {
        String command = "i/1 f/New/Name o/John";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected format exception due to ambiguous friend name");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EDIT_FORMAT_ERROR.getMessage(), e.getMessage());
        }
    }

    // Test duplicate identifier parameters: later identifier overwrites earlier one.
    // Here, "i/2" is used, but if only one activity exists, that should be out of bounds.
    @Test
    public void execute_duplicateIdentifierOverwritesPrevious_exceptionThrown() {
        String command = "i/1 i/2 d/UpdatedDesc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected OUTOFBOUNDS_IDENTIFIER exception due to identifier overwrite");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.OUTOFBOUNDS_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    // Test that an identifier with extra spaces and non-numeric content causes an INVALID_IDENTIFIER exception.
    @Test
    public void execute_identifierWithSpaces_exceptionThrown() {
        String command = "i/  abc  d/NewDesc";
        EditCommand ec = new EditCommand(command);
        try {
            ec.execute(activityManager, false);
            fail("Expected INVALID_IDENTIFIER exception");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_IDENTIFIER.getMessage(), e.getMessage());
        }
    }

    // Test editing a friend’s amount where the amount string has extra spaces and is trimmed correctly.
    @Test
    public void execute_editFriendAmountExtraSpacesAroundAmount_success() {
        String command = "i/1 a/   25.50   o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        assertEquals(25.50, activityManager.getActivity(0).getFriend("John").getAmount(), 0.001);
    }

    // Test duplicate friend name edit parameters: the last occurrence should win.
    @Test
    public void execute_duplicateFriendNameParametersLastOneWins_success() {
        // Both friend edit parameters are provided twice. The map will retain the last values.
        String command = "i/1 f/NewName o/John f/AnotherName o/John";
        EditCommand ec = new EditCommand(command);
        assertDoesNotThrow(() -> ec.execute(activityManager, false));
        // Verify that the friend "John" has been changed to "AnotherName".
        assertNull(activityManager.getActivity(0).getFriend("John"));
        assertNotNull(activityManager.getActivity(0).getFriend("AnotherName"));
    }

    @Test
    public void execute_numbersInPayerName_throwsException() {
        EditCommand cmd = new EditCommand("i/1 n/123");
        try {
            cmd.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NUMBERS_IN_NAME.getMessage(), e.getMessage());
        }
    }

    @Test
    public void execute_numbersInFriendName_throwsException() {
        EditCommand cmd = new EditCommand("i/1 f/123 o/John");
        try {
            cmd.execute(activityManager, false);
            fail();
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.NUMBERS_IN_NAME.getMessage(), e.getMessage());
        }
    }
}
