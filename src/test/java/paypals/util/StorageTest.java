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
import paypals.exception.PayPalsException;

import static org.junit.jupiter.api.Assertions.*;

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

            assertFalse(storage.checkIfFilenameValid("trip/"));
            assertFalse(storage.checkIfFilenameValid("Singapore: 3 day 2 nights"));
            assertFalse(storage.checkIfFilenameValid(""));
            assertFalse(storage.checkIfFilenameValid(" "));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void checkIfFilenameValid_blockWindowsReservedNames() throws PayPalsException {
        Storage storage = new Storage();

        // Reserved device names
        assertFalse(storage.checkIfFilenameValid("CON"), "CON is reserved on Windows");
        assertFalse(storage.checkIfFilenameValid("PRN"), "PRN is reserved on Windows");
        assertFalse(storage.checkIfFilenameValid("AUX"), "AUX is reserved on Windows");
        assertFalse(storage.checkIfFilenameValid("NUL"), "NUL is reserved on Windows");
        assertFalse(storage.checkIfFilenameValid("COM1"), "COM1 is reserved on Windows");
        assertFalse(storage.checkIfFilenameValid("LPT9"), "LPT9 is reserved on Windows");

        // Invalid characters
        assertFalse(storage.checkIfFilenameValid("file:name.txt"), "':' is invalid on Windows");
        assertFalse(storage.checkIfFilenameValid("file|name.txt"), "'|' is invalid on Windows");
        assertFalse(storage.checkIfFilenameValid("file?name.txt"), "'?' is invalid on Windows");
        assertFalse(storage.checkIfFilenameValid("file*name.txt"), "'*' is invalid on Windows");
        assertFalse(storage.checkIfFilenameValid("file\"name.txt"), "'\"' is invalid on Windows");
        assertFalse(storage.checkIfFilenameValid("file<name.txt"), "'<' is invalid on Windows");
        assertFalse(storage.checkIfFilenameValid("file>name.txt"), "'>' is invalid on Windows");

        // Length exceeding MAX_PATH (260 characters)
        String longName = "a".repeat(261);
        assertFalse(storage.checkIfFilenameValid(longName), "Exceeds Windows MAX_PATH");
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void checkIfFilenameValid_blockUnixSpecificInvalidNames() throws PayPalsException {
        Storage storage = new Storage();

        // Invalid characters (forward slash)
        assertFalse(storage.checkIfFilenameValid("file/name.txt"), "'/' is invalid on Unix");

        // Null character (invalid on all OSes but explicitly test here)
        assertFalse(storage.checkIfFilenameValid("file\0name.txt"), "'\\0' is invalid on Unix");
    }
}
