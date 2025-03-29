package paypals;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityManager {
    private String groupName;

    private boolean isNewGroup;

    private ArrayList<Activity> activities;

    public ActivityManager() {
        activities = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean checkIsNewGroup() {
        return isNewGroup;
    }

    public void setGroupDetails(String name, boolean newGroup) {
        this.groupName = name;
        this.isNewGroup = newGroup;
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

    public static ArrayList<Activity> getActivities(String name, ArrayList<Activity> allActivities) {
        ArrayList<Activity> personActivities = new ArrayList<>();
        for (Activity activity : allActivities) {
            if (activity.getPayer().getName().equals(name)) {
                personActivities.add(activity);
            } else {
                HashMap<String, Person> owed = activity.getOwed();
                if (owed.containsKey(name)) {
                    personActivities.add(activity);
                }
            }
        }
        return personActivities;
    }

    public int getIdentifierFromUnpaidList(Activity activity, String name) {
        ArrayList<Activity> personActivities = getActivities(name, activities);
        int identifier = 1;
        for (Activity personActivity : activities) {
            if (!personActivity.isActivityFullyPaid(name, false)) {
                if (personActivity.equals(activity)) {
                    return identifier;
                }
                identifier++;
            }
        }
        return -1;
    }

}
