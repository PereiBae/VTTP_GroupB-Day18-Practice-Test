package vttp.ssf.practice_test.models;

import jakarta.validation.constraints.NotBlank;

public class Person {

    @NotBlank
    private String fullName;

    private int age;

    public Person() {
    }

    public Person(int age, String fullName) {
        this.age = age;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

}
