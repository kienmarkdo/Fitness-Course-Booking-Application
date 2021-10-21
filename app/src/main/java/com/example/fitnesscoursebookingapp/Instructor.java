package com.example.fitnesscoursebookingapp;

import java.util.ArrayList;

/**
 * Instructor users are able to select the type of class
 * they would like to teach and then specify the class day, class time, class difficulty, and class
 * capacity
 */
public class Instructor extends User {

    private String legalName;
    private String description;
    ArrayList<Course> courseTeaching;

    /** Constructor */
    public Instructor(String username, String password) {
        super(username, password);
        usertype = "instructor";
    }

    public Instructor(String username, String password, String legalName) {
        super(username, password);
        usertype = "instructor";
        this.legalName = legalName;
    }

    public Instructor(String username, String password, String legalName, boolean isBothTypes) {
        super(username, password);
        usertype = isBothTypes ? "both" : "instructor";
        this.legalName = legalName;
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

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getDescription() {
        return description;
    }

    public Course[] getCourseTeaching() {
        return null;
    }

    public void addCourseTeaching(Course c) { courseTeaching.add(c); }
}
