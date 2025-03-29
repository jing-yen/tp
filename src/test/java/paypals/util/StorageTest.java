package paypals.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;

import paypals.Activity;
import paypals.ActivityManager;
import paypals.PayPalsTest;
import paypals.commands.AddCommand;

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
            System.out.println(f.getCanonicalFile());
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
}
