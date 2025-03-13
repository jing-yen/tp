package paypals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Activity {
    private String description;
    private Person payer;
    private Map<Person, Integer> owed;

    public Activity(String description, String name, HashMap<String, Integer> owed) {
        this.description = description;
        this.payer = PersonManager.getPerson(name);
        this.owed = owed.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> PersonManager.getPerson(entry.getKey()),
                        entry -> entry.getValue()));
    }
}
