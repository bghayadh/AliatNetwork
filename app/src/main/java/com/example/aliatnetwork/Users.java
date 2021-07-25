package com.example.aliatnetwork;

public class Users {
    private String FirstName;
    private String MiddleName;
    private String LastName;

    public Users(String firstName, String middleName, String lastName) {

        FirstName = firstName;
        MiddleName = middleName;
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
