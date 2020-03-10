package db;

import models.Person;
import utils.GeneratorUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Queries {
    static final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    public static boolean createPersonTable() {
        String sql =  "CREATE TABLE   PERSONS " +
                "(id BIGINT not NULL, " +
                " name VARCHAR(255), " +
                " age INTEGER, " +
                " PRIMARY KEY ( id ))";
        Connection connection = SqlConnectionManager.getConnection(DB_URL, USER, PASS);
        return SqlStatement.executeUpdateStatement(connection,sql);
    }

    public static boolean insertPerson(Person person) {

        String sql = "INSERT INTO PERSONS " + "VALUES (" + person.getUuid() + ", " +person.getName() +", " + person.getAge();
        Connection connection = SqlConnectionManager.getConnection(DB_URL, USER, PASS);
        return SqlStatement.executeUpdateStatement(connection,sql);
    }

    public static long getCountAllPersons() {
        List<Person> results = getAllPersons();
        return results == null || results.isEmpty() ? 0 : results.size();
    }

    public static List<Person> getAllPersons() {
        String sql = "SELECT * FROM PERSONS";
        Connection connection = SqlConnectionManager.getConnection(DB_URL, USER, PASS);
        List<Map<String, Object>> resultsQuery = SqlStatement.executeSelectStatement(connection,sql);
        List<Person> results = new ArrayList<>();
        resultsQuery.forEach(entity->{
            long uuid = Long.parseLong(String.valueOf(entity.get("uuid")));
            String name = String.valueOf(entity.get("name"));
            int age = (Integer) entity.get("age");
            results.add(new Person(uuid, name, age));
        });
        return results;
    }
}
