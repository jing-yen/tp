package paypals;

import java.util.ArrayList;

public class ActivityManager {
    private ArrayList<Activity> activities;

    public ActivityManager() {
        activities = new ArrayList<>();
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
}
