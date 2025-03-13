package paypals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Activity {
    private int totalAmount;
    private String description;
    private Person payer;
    private Map<Person, Integer> owed;

    public Activity(String description, String name, HashMap<String, Integer> owed, PersonManager personManager) {
        this.description = description;
        this.payer = personManager.getPerson(name);
        this.owed = owed.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> personManager.getPerson(entry.getKey()),
                        entry -> entry.getValue()));
    }
}
