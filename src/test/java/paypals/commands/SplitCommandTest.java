package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.PayPalsTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SplitCommandTest extends PayPalsTest {

    @Test
    public void testSplitCommand_oneActivityOneFriend_oneResult() {
        callCommand(new AddCommand("d/lunch n/John f/Jane a/28"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "Jane pays John $28.00";
        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void testSplitCommand_oneActivityTwoFriends_twoResults() {
        callCommand(new AddCommand("d/lunch n/John f/Jane a/28 f/Jeremy a/10"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "Jane pays John $28.00\n" +
                "Jeremy pays John $10.00\n";
        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void testSplitCommand_twoActivities_twoResults() {
        callCommand(new AddCommand("d/lunch n/John f/Jane a/28 f/Jeremy a/10"));
        callCommand(new AddCommand("d/lunch n/John f/Jane a/18"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "Jane pays John $46.00\n" +
                "Jeremy pays John $10.00\n";
        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void testSplitCommand_twoActivitiesWithDifferentCaseNames_twoResults() {
        callCommand(new AddCommand("d/transaction 1 n/John f/Jane a/50 f/Eric a/30"));
        callCommand(new AddCommand("d/transaction 2 n/jOHn f/jANe a/3 f/eric a/5"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "jANe pays jOHn $53.00\n" +
                "eric pays jOHn $35.00\n";

        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void testSplitCommand_mixedPayersAndCreditorsWithMoreTransactions_correctAggregation() {
        // Add multiple transactions with mixed-case names for both payers and creditors
        callCommand(new AddCommand("d/transaction 1 n/John f/Jane a/50 f/Eric a/30 f/Alice a/20"));
        callCommand(new AddCommand("d/transaction 2 n/jOhN f/jANe a/10 f/ERIC a/20 f/ALICE a/10"));
        callCommand(new AddCommand("d/transaction 3 n/Jane f/JOHN a/15 f/eric a/25 f/Bob a/10"));
        callCommand(new AddCommand("d/transaction 4 n/ERIC f/JANE a/10 f/bOb a/5"));
        callCommand(new AddCommand("d/transaction 5 n/Alice f/jane a/5 f/ERIC a/5"));
        callCommand(new AddCommand("d/transaction 6 n/Bob f/John a/20 f/JANE a/10 f/eric a/5"));
        callCommand(new AddCommand("d/transaction 7 n/AlIce f/BOB a/15 f/JOHN a/5 f/ERIC a/10"));
        callCommand(new AddCommand("d/transaction 8 n/JOHN f/ALICE a/10 f/BOB a/5 f/JANE a/5"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "ERIC pays JOHN $80.00\n" +
                "JANE pays JOHN $40.00";

        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void testSplitCommand_identicalCaseNames_noDuplicateEntries() {
        // Add transactions with identical case-sensitive names
        callCommand(new AddCommand("d/transaction 1 n/John f/Jane a/30 f/Eric a/20"));
        callCommand(new AddCommand("d/transaction 2 n/John f/Jane a/10 f/Eric a/10"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "Jane pays John $40.00\n" +
                "Eric pays John $30.00\n";

        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void testSplitCommand_allLowercaseOrUppercaseNames_consistentHandling() {
        // Add transactions with all-lowercase and all-uppercase names
        callCommand(new AddCommand("d/transaction 1 n/john f/jane a/50 f/eric a/30"));
        callCommand(new AddCommand("d/transaction 2 n/JOHN f/JANE a/10 f/ERIC a/20"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "JANE pays JOHN $60.00\n" +
                "ERIC pays JOHN $50.00\n";

        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void testSplitCommand_largeNumberOfTransactions_correctAggregation() {
        // Add a large number of transactions with mixed-case names
        for (int i = 1; i <= 100; i++) {
            callCommand(new AddCommand("d/transaction " + i + " n/John f/Jane a/1 f/Eric a/1"));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        callCommand(new SplitCommand(""));

        System.setOut(System.out);

        String expectedOutput = "Best way to settle debts:\n" +
                "Eric pays John $100.00\n" +
                "Jane pays John $100.00\n";

        assertEquals(expectedOutput.trim().replace("\r\n", "\n"), outputStream.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void isExit_someInput_expectFalse() {
        SplitCommand command = new SplitCommand("");

        assertFalse(command.isExit(), "isExit() should return false for a SplitCommand");
    }
}
