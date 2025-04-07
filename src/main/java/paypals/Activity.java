package paypals;

import paypals.util.Logging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Activity {
    private String description;
    private Person payer;
    // Key: name (lower-cased) and Value: name (normal-cased)
    private HashMap<String, String> names;
    // Key: name (normal-cased) and Value: Person object
    private HashMap<String, Person> owed;

    public Activity(String description, Person payer, HashMap<String, Double> owedMap) {
        this.description = description;
        this.payer = payer;
        this.names = new HashMap<>();
        this.owed = new HashMap<>();

        Logging.logInfo("Creating Activity: " + description + " paid by " + payer.getName());

        for (Map.Entry<String, Double> entry : owedMap.entrySet()) {
            String name = entry.getKey();
            String lowercaseName = name.toLowerCase();
            Double valueOwed = entry.getValue();

            this.names.put(lowercaseName, name);
            this.owed.put(name, new Person(name, valueOwed, false));
            Logging.logInfo("Added owed person: " + name + " with amount " + valueOwed);
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

        // Obtain the name in correct case from name parameter (which may have alphabets of wrong case)
        String lowercaseName = name.toLowerCase();
        String correctcaseName = names.get(lowercaseName);

        return owed.get(correctcaseName);
    }

    public Collection<Person> getAllFriends() {
        Logging.logInfo("Retrieving all friends who owe money");
        return owed.values();
    }

    public HashMap<String, Person> getOwed() {
        return owed;
    }

    public HashMap<String, String> getNames() {
        return names;
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


    /**
     * Converts the activity into a storage-friendly string format, using the specified separator.
     *
     * @param separator the delimiter used to separate data fields
     * @return a string representation of the activity in storage format
     */
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

    /**
     * Updates the list of people who owe money for this activity.
     *
     * @param owed the new map of people who owe money
     */
    public void setOwed(HashMap<String, Person> owed) {
        Logging.logInfo("Updating owed list for activity: " + description);
        this.owed = owed;
    }

    public void editDesc(String newDesc) {
        this.description = newDesc;
    }

    public void editPayer(String newPayer) {
        this.payer.editName(newPayer);
    }

    public String editOwedName(String name, String newName) {
        String lowercaseName = name.toLowerCase();
        String lowercaseNewName = newName.toLowerCase();
        // Old name (in correct case) to be returned
        String oldName = names.get(lowercaseName);

        // Remove and add the Person object into owed HashMap after changing name (normal-case)
        Person newPerson = this.owed.remove(name);
        if (newPerson != null) {
            newPerson.editName(newName);
            this.owed.put(newName, newPerson);
        }

        // Remove old name and add the new name into names HashMap
        this.names.remove(lowercaseName);
        this.names.put(lowercaseNewName, newName);

        return oldName;
    }

    public void editOwedAmount(String name, double newAmount) {
        // Obtain the name in correct case from name parameter (which may have alphabets of wrong case)
        String lowercaseName = name.toLowerCase();
        String correctcaseName = names.get(lowercaseName);

        this.owed.get(correctcaseName).editAmount(newAmount);
    }

    /**
     * Checks whether the activity is fully paid.
     *
     * <p>If {@code checkAllPayer} is {@code true}, it checks if all people have paid their dues.
     * Otherwise, it checks if the specified person has paid their dues.</p>
     *
     * @param name        the name of the person to check (ignored if {@code checkAllPayer} is {@code true})
     * @param checkAllPayer whether to check if all people have paid their dues
     * @return {@code true} if the activity is fully paid, {@code false} otherwise
     */
    public boolean isActivityFullyPaid(String name, boolean checkAllPayer) {
        String lowercaseName = name.toLowerCase();
        String lowercasePayer = payer.getName().toLowerCase();

        if (checkAllPayer || lowercasePayer.equals(lowercaseName)) {
            for (Person friend : owed.values()) {
                if (!friend.hasPaid()) {
                    return false;
                }
            }
            return true;
        }
        return getFriend(name).hasPaid();
    }

}
