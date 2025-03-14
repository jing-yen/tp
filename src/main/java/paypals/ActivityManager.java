package paypals;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityManager {
    private ArrayList<Activity> activities;
    private HashMap<String, Double> netOwedMap;

    public ActivityManager() {
        activities = new ArrayList<>();
        netOwedMap = new HashMap<>();
    }

    public HashMap<String, Double> getNetOwedMap() {
        return this.netOwedMap;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }
}
