package paypals;

import java.util.HashMap;
import java.util.Map;

public class Activity {
    private String description;
    private Person payer;
    private Map<String, Person> owed;

    public Activity(String description, String name, HashMap<String, Double> owedMap) {
        this.description = description;
        this.payer = PersonManager.getPerson(name);
        /*this.owed = owed.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> PersonManager.getPerson(entry.getKey()),
                        entry -> entry.getValue()));*/
        this.owed = new HashMap<>();
        for (Map.Entry<String, Double> entry : owedMap.entrySet()) {
            this.owed.put(entry.getKey(), new Person(entry.getKey(), entry.getValue(), false));
        }
    }

    public Person getPayer() {
        return payer;
    }

    public Map<String, Person> getOwed() {
        return owed;
    }

    @Override
    public String toString() {
        String outputString = description + " paid by " + payer.getName() +
                ". Owed by: ";
        int personCount = 0;
        for (Person person : owed.values()) {
            outputString += person.toString(false);
            if (personCount++ < owed.size() - 1) {
                outputString += ", ";
            }
        }
        return outputString.toString();
    }

    public String toStorageString(String separator) {
        String data = this.description + separator + this.payer + separator;
        data += payer.getName() + separator + payer.getAmount() + separator + payer.hasPaid() + separator;
        for (Map.Entry<String, Person> entry : owed.entrySet()) {
            Person person = entry.getValue();
            data += person.getName() + separator + person.getAmount() + separator + person.hasPaid() + separator;
        }
        return data;
    }

    public Person getPerson(String name) {
        return owed.get(name);
    }

    public String getDescription(){
        return description;
    }

}
