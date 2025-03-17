package paypals.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.commands.AddCommand;

public class StorageTest {
    @Test
    public void storageConstructor_fileCreationSuccess() {
        try {
            Storage storage = new Storage();
            File f = new File("./data");
            File g;
            g = new File("./data/savefile.txt");
            assertTrue(g.exists());
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
            ActivityManager result = new ActivityManager();
            AddCommand c = new AddCommand("add d/tickets n/John f/Betty a/23.53 f/Jane a/20.21 f/Bob a/38.10");
            c.execute(activityManager);
            c = new AddCommand("add d/lunch n/John f/Jane a/28");
            c.execute(activityManager);
            storage.save(activityManager);
            storage.load(result);
            ArrayList<Activity> expected = activityManager.getActivityList();
            ArrayList<Activity> actual = result.getActivityList();
            assertEquals(expected.size(), actual.size(), "The number of activities should match");
            storage.deleteDir(f);
        } catch (Exception e) {
            fail();
        }
    }
}
