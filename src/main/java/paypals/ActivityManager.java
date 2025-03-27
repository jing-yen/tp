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

    public void editActivityDesc(int activityId, String newDesc) {
        activities.get(activityId).editDesc(newDesc);
    }

    public void editActivityPayer(int activityId, String newPayer) {
        activities.get(activityId).editPayer(newPayer);
    }

    public void editActivityOwedName(int activityId, String name, String newName) {
        activities.get(activityId).editOwedName(name, newName);
    }

    public void editActivityOwedAmount(int activityId, String name, double newAmount) {
        activities.get(activityId).editOwedAmount(name, newAmount);
    }
}
