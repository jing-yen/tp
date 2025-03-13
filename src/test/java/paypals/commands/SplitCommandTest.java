package paypals.commands;

import org.junit.jupiter.api.Test;
import paypals.ActivityManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SplitCommandTest {
    ActivityManager activityManager = new ActivityManager();

    @Test
    void testSplitCommand_nullInput_twoSystemPrintLines() {
        AddCommand addCommand = new AddCommand("d/lunch n/John f/Jane /a28 f/Bob /a30");
        addCommand.execute(activityManager);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        SplitCommand splitCommand = new SplitCommand("");

        System.setOut(System.out);

        String expectedOutput = """
                Best way to settle debts:
                Jane pays John $28
                Bob pays John $30
                """;

        assertEquals(expectedOutput, outputStream.toString().trim());
    }
}
