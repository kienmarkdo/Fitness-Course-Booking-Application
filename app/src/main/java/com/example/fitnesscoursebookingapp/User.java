package com.example.fitnesscoursebookingapp;

/**
 * Extended by classes like Instructor and GymMember
 */
public abstract class User {

    protected String username; // NOTE: The username is also a unique identifier for a user ***AND CANNOT BE CHANGED***
    protected String password;

    /** Constructor */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /** Getter and Setter methods */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
