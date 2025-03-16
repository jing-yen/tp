package paypals;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityManager {
    private ArrayList<Activity> activities;
    private HashMap<String, Double> netOwedMap;
    private HashMap<String, ArrayList<Activity>> personActivitiesMap;

    public ActivityManager() {
        activities = new ArrayList<>();
        netOwedMap = new HashMap<>();
        personActivitiesMap = new HashMap<>();
    }

    public HashMap<String, Double> getNetOwedMap() {
        return this.netOwedMap;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public int getSize() {
        return activities.size();
    }


    public Activity getActivity(int id) {
        return activities.get(id);
    }

    public void deleteActivity(int id) {
        activities.remove(id);
    }

    public ArrayList<Activity> getActivityList() {
        return this.activities;
    }

    public HashMap<String, ArrayList<Activity>> getPersonActivitiesMap() {
        return this.personActivitiesMap;
    }
}
