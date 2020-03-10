import db.Queries;
import models.Person;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Queries.createPersonTable();
        Person person = new Person("user1", 19);
        Queries.insertPerson(person);
        System.out.println(Queries.getCountAllPersons());
        List<Person> results =  Queries.getAllPersons();
        for (Person result : results) {
            System.out.println(person.getUuid() + " / " + person.getName() + " / " + person.getAge());
        }
    }
}
