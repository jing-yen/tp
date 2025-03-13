package paypals;

import java.util.ArrayList;

public class ActivityManager {
    private ArrayList<Activity> activities;
    private ArrayList<Person> netOwed;

    public ActivityManager() {
        activities = new ArrayList<>();
        netOwed = new ArrayList<>();
    }

    public ArrayList<Person> getNetOwed() {
        return this.netOwed;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }
}
