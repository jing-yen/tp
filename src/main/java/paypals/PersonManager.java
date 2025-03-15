package paypals;

import java.util.HashMap;

public class PersonManager {
    private static HashMap<String, Person> personList = new HashMap<String, Person>();

    private static void addPerson(String name) {
        personList.put(name, new Person(name, 0));
    }

    static Person getPerson(String name) {
        if (!personList.containsKey(name)) {
            addPerson(name);
        }
        return personList.get(name);
    }

}
