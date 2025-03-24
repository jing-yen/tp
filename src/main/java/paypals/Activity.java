package paypals;

import paypals.util.Logging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Activity {
    private String description;
    private Person payer;
    private HashMap<String, Person> owed;

    public Activity(String description, Person payer, HashMap<String, Double> owedMap) {
        this.description = description;
        this.payer = payer;
        this.owed = new HashMap<>();

        Logging.logInfo("Creating Activity: " + description + " paid by " + payer.getName());


        for (Map.Entry<String, Double> entry : owedMap.entrySet()) {
            this.owed.put(entry.getKey(), new Person(entry.getKey(), entry.getValue(), false));
            Logging.logInfo("Added owed person: " + entry.getKey() + " with amount " + entry.getValue());
        }
    }

    public String getDescription(){
        Logging.logInfo("Retrieving description: " + description);
        return description;
    }

    public Person getPayer() {
        Logging.logInfo("Retrieving payer: " + payer.getName());
        return payer;
    }

    public Person getFriend(String name) {
        Logging.logInfo("Looking for friend: " + name);
        return owed.get(name);
    }

    public Collection<Person> getAllFriends() {
        Logging.logInfo("Retrieving all friends who owe money");
        return owed.values();
    }

    @Override
    public String toString() {
        String spacing = "    ";
        String outputString = "Desc: " + description + "\n"
                + spacing + "Payer: " + payer.getName() + "\n"
                + spacing + "Owed by: ";
        int personCount = 0;
        for (String name : owed.keySet()) {
            outputString += name;
            if (personCount++ < owed.size() - 1) {
                outputString += ", ";
            }
        }
        Logging.logInfo("Generated string representation of Activity: " + description);
        return outputString;
    }

    public String toStorageString(String separator) {
        String data = this.description + separator;
        data += payer.getName() + separator + payer.getAmount() + separator + payer.hasPaid() + separator;
        for (Map.Entry<String, Person> entry : owed.entrySet()) {
            Person person = entry.getValue();
            data += person.getName() + separator + person.getAmount() + separator + person.hasPaid() + separator;
        }
        Logging.logInfo("Converted Activity to storage format: " + description);
        return data;
    }

    public void setOwed(HashMap<String, Person> owed) {
        Logging.logInfo("Updating owed list for activity: " + description);
        this.owed = owed;
    }

}
