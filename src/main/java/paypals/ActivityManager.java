package paypals;

import java.util.ArrayList;
import java.util.Comparator;

public class ActivityManager {
    private ArrayList<Activities> activitiesArray;
    private ArrayList<Person> netOwed;

    public ActivityManager() {
        activitiesArray = new ArrayList<>();
        netOwed = new ArrayList<>();
    }

    public ArrayList<Person> getNetOwed() {
        return this.netOwed;
    }

}
