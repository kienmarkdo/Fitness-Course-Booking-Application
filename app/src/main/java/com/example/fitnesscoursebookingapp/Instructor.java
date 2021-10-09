package com.example.fitnesscoursebookingapp;

import java.util.ArrayList;

/**
 * Instructor users are able to select the type of class
 * they would like to teach and then specify the class day, class time, class difficulty, and class
 * capacity
 */
public class Instructor extends User {

    private String name;
    private String description;
    ArrayList<Course> courseTeaching;

    /** Constructor */
    public Instructor(String username, String password) {
        super(username, password);
    }

    /** Class methods */

    private void selectCourseToTeach() {

    }

    /** Getter and Setter methods */
    private void setCourseDifficulty() {

    }

    private void setDateAndTime() {

    }

    private void setCapacity() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Course[] getCourseTeaching() {
        return null;
    }
}
