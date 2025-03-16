package paypals;

import java.util.HashMap;

public class PersonManager {
    private HashMap<String, Person> personList = new HashMap<String, Person>();

    private void addPerson(String name) {
        personList.put(name, new Person(name, 0,false));
    }

    Person getPerson(String name) {
        if (!personList.containsKey(name)) {
            addPerson(name);
        }
        return personList.get(name);
    }

}
