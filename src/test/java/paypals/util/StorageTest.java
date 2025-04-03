package paypals.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.io.FileWriter;

import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import paypals.Activity;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.commands.AddCommand;
import paypals.commands.PaidCommand;
import paypals.exception.ExceptionMessage;
import paypals.exception.PayPalsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    public void checkIfFilenameValid_blockInvalidNames() throws PayPalsException {
        Storage storage = new Storage();

        PayPalsException ex1 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid(""));
        assertEquals(ExceptionMessage.EMPTY_FILENAME.getMessage(), ex1.getMessage());

        PayPalsException ex2 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid(" "));
        assertEquals(ExceptionMessage.EMPTY_FILENAME.getMessage(), ex2.getMessage());

        PayPalsException ex3 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("      "));
        assertEquals(ExceptionMessage.EMPTY_FILENAME.getMessage(), ex3.getMessage());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void checkIfFilenameValid_blockWindowsReservedNames() throws PayPalsException {
        Storage storage = new Storage();

        // Reserved device names
        PayPalsException ex1 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("CON"));
        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex1.getMessage());

        PayPalsException ex2 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("PRN"));
        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex2.getMessage());

        PayPalsException ex3 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("AUX"));
        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex3.getMessage());

        PayPalsException ex4 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("NUL"));
        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex4.getMessage());

        PayPalsException ex5 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("COM1"));
        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex5.getMessage());

        PayPalsException ex6 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("LPT9"));
        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex6.getMessage());

        // Invalid characters
        PayPalsException ex7 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file:name.txt"));
        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex7.getMessage());

        PayPalsException ex8 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file|name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex8.getMessage());

        PayPalsException ex9 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file?name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex9.getMessage());

        PayPalsException ex10 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file*name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex10.getMessage());

        PayPalsException ex11 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file\"name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex11.getMessage());

        PayPalsException ex12 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file<name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex12.getMessage());

        PayPalsException ex13 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file>name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex13.getMessage());

        // Length exceeding MAX_PATH (260 characters)
        String longName = "a".repeat(261);
        PayPalsException ex14 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid(longName));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex14.getMessage());
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void checkIfFilenameValid_blockUnixSpecificInvalidNames() throws PayPalsException {
        Storage storage = new Storage();

        // Invalid characters (forward slash)
        PayPalsException ex1 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file/name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex1.getMessage());

        // Null character (invalid on all OSes but explicitly test here)
        PayPalsException ex2 = assertThrows(PayPalsException.class,
                () -> storage.checkIfFilenameValid("file\0name.txt"));
        assertEquals(ExceptionMessage.INVALID_FILENAME.getMessage(), ex2.getMessage());
    }

    @Test
    void checkIfFilenameValid_blockInvalidNumbers() throws PayPalsException {
        Storage storage = new Storage();

        PayPalsException ex1 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("9"));
        assertEquals(ExceptionMessage.INVALID_GROUP_NUMBER.getMessage(), ex1.getMessage());

        PayPalsException ex2 = assertThrows(PayPalsException.class, () -> storage.checkIfFilenameValid("-1"));
        assertEquals(ExceptionMessage.INVALID_GROUP_NUMBER.getMessage(), ex2.getMessage());
    }

    @Test
    public void deleteGroup_existingGroup_fileRemovedSuccessfully() throws PayPalsException {
        Storage storage = new Storage();
        ActivityManager activityManager = new ActivityManager();
        storage.load("testgroup", activityManager);
        File file = new File("./data/testgroup.txt");
        assertTrue(file.exists());

        storage.delete("testgroup", activityManager);
        assertFalse(file.exists(), "File should be deleted after group deletion");

        storage.deleteDir(new File("./data"));
    }

    @Test
    public void deleteGroup_byNumber_fileRemovedSuccessfully() throws PayPalsException {
        Storage storage = new Storage();
        ActivityManager activityManager = new ActivityManager();
        storage.load("test2", activityManager); // adds to groupNames at index 0

        assertTrue(storage.containsFile("1"), "Should recognize group number 1");
        storage.delete("1", activityManager);

        File file = new File("./data/test2.txt");
        assertFalse(file.exists(), "Group file should be deleted");

        storage.deleteDir(new File("./data"));
    }

    @Test
    public void containsFile_invalidName_throwsException() throws PayPalsException {
        Storage storage = new Storage();

        PayPalsException ex = assertThrows(PayPalsException.class,
                () -> storage.containsFile("NotExists123"));

        assertEquals(ExceptionMessage.FILENAME_DOES_NOT_EXIST.getMessage(), ex.getMessage());
    }

    @Test
    public void containsFile_invalidNumberOutOfBounds_throwsException() throws PayPalsException {
        Storage storage = new Storage();
        PayPalsException ex = assertThrows(PayPalsException.class, () -> storage.containsFile("999"));
        assertEquals(ExceptionMessage.INVALID_GROUP_NUMBER.getMessage(), ex.getMessage());
    }

    @Test
    public void save_emptyActivityList_writesNothing() throws Exception {
        Storage storage = new Storage();
        ActivityManager manager = new ActivityManager();

        storage.load("emptySave", manager);
        storage.save(manager);

        File f = new File("./data/emptySave.txt");
        assertTrue(f.exists() && f.length() == 0, "Save file should be empty");

        storage.deleteDir(new File("./data"));
    }

    @Test
    public void load_existingGroupByNumber_loadsSuccessfully() throws PayPalsException {
        Storage storage = new Storage();
        ActivityManager am = new ActivityManager();

        storage.load("GroupByNumber", am); // adds it
        storage.load("1", am); // should resolve to same group

        assertEquals("GroupByNumber", am.getGroupName());
        storage.deleteDir(new File("./data"));
    }

    @Test
    public void load_malformedStorageLine_throwsLoadError() throws Exception {
        Storage storage = new Storage();
        ActivityManager am = new ActivityManager();

        // Create a broken save file manually
        File file = new File("./data/BrokenGroup.txt");
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("invalid data without enough parts\n");
        }

        // Write group name into master file
        File master = new File("./data/master-savefile.txt");
        try (FileWriter masterWriter = new FileWriter(master, true)) {
            masterWriter.write("BrokenGroup\n");
        }

        PayPalsException ex = assertThrows(PayPalsException.class, () -> storage.load("BrokenGroup", am));
        assertEquals(ExceptionMessage.LOAD_ERROR.getMessage(), ex.getMessage());

        storage.deleteDir(new File("./data"));
    }

    @Test
    public void checkIfFilenameValid_validName_returnsTrue() throws PayPalsException {
        Storage storage = new Storage();
        boolean result = storage.checkIfFilenameValid("valid_filename123");
        assertTrue(result);
        storage.deleteDir(new File("./data"));
    }

    @Test
    public void checkIfFilenameValid_validGroupNumber_returnsTrue() throws PayPalsException {
        Storage storage = new Storage();
        ActivityManager am = new ActivityManager();
        storage.load("SomeGroup", am);
        assertTrue(storage.checkIfFilenameValid("1"));
        storage.deleteDir(new File("./data"));
    }

    @Test
    public void processLine_invalidBooleanValue_ignoresInvalidPaidFlag() throws Exception {
        Storage storage = new Storage();
        ActivityManager am = new ActivityManager();
        File file = new File("./data/testflag.txt");
        file.getParentFile().mkdirs();
        file.createNewFile();

        try (FileWriter fw = new FileWriter(file)) {
            // Has 1 friend, invalid boolean at end
            String sep = String.valueOf(Character.toChars(31));
            String line = "Lunch" + sep + "John" + sep + "dummy" + sep + "dummy" +
                    sep + "Bob" + sep + "20" + sep + "notaboolean\n";
            fw.write(line);
        }

        File master = new File("./data/master-savefile.txt");
        try (FileWriter masterWriter = new FileWriter(master, true)) {
            masterWriter.write("testflag\n");
        }

        assertDoesNotThrow(() -> storage.load("testflag", am));
        assertEquals(1, am.getActivityList().size());

        storage.deleteDir(new File("./data"));
    }

    @Test
    public void save_thenLoad_preservesHasPaidFlag() throws Exception {
        Storage storage = new Storage();
        ActivityManager am1 = new ActivityManager();
        ActivityManager am2 = new ActivityManager();

        AddCommand cmd1 = new AddCommand("d/Coffee n/Ann f/Tom a/5.00");
        cmd1.execute(am1, false);

        PaidCommand paidCmd = new PaidCommand("n/Tom i/1");
        paidCmd.execute(am1, false);

        storage.load("paidTest", am1);
        storage.save(am1);
        storage.load("paidTest", am2);

        assertEquals(1, am2.getActivityList().size());
        assertTrue(am2.getActivity(0).getFriend("Tom").hasPaid());

        storage.deleteDir(new File("./data"));
    }

}
