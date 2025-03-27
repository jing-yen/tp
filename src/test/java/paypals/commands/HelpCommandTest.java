package paypals.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paypals.ActivityManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelpCommandTest {

    private ActivityManager activityManager;

    @BeforeEach
    public void setUp() {
        activityManager = new ActivityManager();
    }

    @Test
    public void execute_helpCommand_printsHelpMessage() {
        // Capture system output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        HelpCommand helpCommand = new HelpCommand("");
        helpCommand.execute(activityManager, true);

        // Convert output to string and assert parts of help message are printed
        String output = outputStream.toString();

        assertTrue(output.contains("Help - Available Commands:"));
        assertTrue(output.contains("add d/DESCRIPTION n/PAYER"));
        assertTrue(output.contains("delete i/IDENTIFIER"));
        assertTrue(output.contains("paid n/NAME i/IDENTIFIER"));
        assertTrue(output.contains("list"));
        assertTrue(output.contains("split"));
        assertTrue(output.contains("help"));
        assertTrue(output.contains("exit"));
    }
}
