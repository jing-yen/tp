package paypals.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import paypals.Activity;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.commands.AddCommand;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest extends PayPalsTest {
    @Test
    public void storageConstructor_fileCreationSuccess() {
        try {
            Storage storage = new Storage();
            File f = new File("./data");
            File g;
            g = new File("./data/master-savefile.txt");
            assertTrue(g.exists());
            storage.deleteDir(f);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void loadData_groupCreationSuccess() {
        try {
            File f = new File("./data");
            Storage storage = new Storage();
            ActivityManager activityManager = new ActivityManager();
            ArrayList<String> groups = new ArrayList<>();
            groups.add("Friends");
            groups.add("family");
            groups.add("sing🎌 Japan");
            groups.add("1 More group");
            for (String group : groups) {
                storage.load(group, activityManager);
                File g;
                g = new File(String.format("./data/%s.txt", group));
                assertTrue(g.exists());
            }
            storage.deleteDir(f);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void loadData_dataLoaded_success() {
        try {
            Storage storage = new Storage();
            File f = new File("./data");
            ActivityManager activityManager = new ActivityManager();
            storage.load("trip", activityManager);
            ActivityManager result = new ActivityManager();
            AddCommand c = new AddCommand("add d/tickets n/John f/Betty a/23.53 f/Jane a/20.21 f/Bob a/38.10");
            c.execute(activityManager, false);
            c = new AddCommand("add d/lunch n/John f/Jane a/28");
            c.execute(activityManager, false);
            storage.save(activityManager);
            storage.load("trip", result);
            ArrayList<Activity> expected = activityManager.getActivityList();
            ArrayList<Activity> actual = result.getActivityList();
            assertEquals(expected.size(), actual.size(), "The number of activities should match");
            storage.deleteDir(f);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void checkIfFilenameValid_blockInvalidNames() {
        try {
            Storage storage = new Storage();
            storage.checkIfFilenameValid("");
            storage.checkIfFilenameValid(" ");
            storage.checkIfFilenameValid("      ");
            fail("Expected a PayPalsException to be thrown");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EMPTY_FILENAME.getMessage(), e.getMessage());
        }
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void checkIfFilenameValid_blockWindowsReservedNames() throws PayPalsException {
        try {
            Storage storage = new Storage();

            // Reserved device names
            storage.checkIfFilenameValid("CON");
            storage.checkIfFilenameValid("PRN");
            storage.checkIfFilenameValid("AUX");
            storage.checkIfFilenameValid("NUL");
            storage.checkIfFilenameValid("COM1");
            storage.checkIfFilenameValid("LPT9");

            //Invalid characters
            storage.checkIfFilenameValid("file:name.txt");
            storage.checkIfFilenameValid("file|name.txt");
            storage.checkIfFilenameValid("file?name.txt");
            storage.checkIfFilenameValid("file*name.txt");
            storage.checkIfFilenameValid("file\"name.txt");
            storage.checkIfFilenameValid("file<name.txt");
            storage.checkIfFilenameValid("file>name.txt");

            // Length exceeding MAX_PATH (260 characters)
            String longName = "a".repeat(261);
            storage.checkIfFilenameValid(longName);

            fail("Expected a PayPalsException to be thrown");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.EMPTY_FILENAME.getMessage(), e.getMessage());
        }
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void checkIfFilenameValid_blockUnixSpecificInvalidNames() {
        try {
            Storage storage = new Storage();
            // Invalid characters (forward slash)
            storage.checkIfFilenameValid("file/name.txt");
            // Null character (invalid on all OSes but explicitly test here)
            storage.checkIfFilenameValid("file\0name.txt");
            fail("Expected a PayPalsException to be thrown");
        } catch (PayPalsException e) {
            assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), e.getMessage());
        }
    }
}
