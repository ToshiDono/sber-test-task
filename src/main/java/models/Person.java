package models;

import utils.GeneratorUtils;

public class Person {
    public static int size = 0;
    private long uuid;
    private String name;
    int age;

    public Person(String name, int age) {
        this.uuid = ++size;
        this.name = name;
        this.age = age;

    }

    public Person(long uuid, String name, int age) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
