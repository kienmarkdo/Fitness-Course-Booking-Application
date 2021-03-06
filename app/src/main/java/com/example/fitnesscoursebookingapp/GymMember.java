package com.example.fitnesscoursebookingapp;

import java.util.LinkedList;
import java.util.ArrayList;
/**
 * Represents a Gym Member that participates in the Courses within this Gym
 *
 */
public class GymMember extends User {

    private String legalName;
    ArrayList<Course> coursesAttending;

    /** Constructor */

    public GymMember() {

    }

    public GymMember(String username, String password) {
        super(username, password);
        usertype = "gymMember";
    }

    public GymMember(String username, String password, String legalName) {
        super(username, password);
        usertype = "gymMember";
        this.legalName = legalName;
    }

    public GymMember(String username, String password, String legalName, boolean isBothTypes) {
        super(username, password);
        usertype = isBothTypes ? "both" : "gymMember";
        this.legalName = legalName;
    }

    public GymMember(String username, String password, String legalName, ArrayList<Course> coursesAttending) {
        super(username, password);
        usertype = "gymMember";
        this.legalName = legalName;
        this.coursesAttending = coursesAttending;
    }

    /** Class methods */

    public void selectCourseToEnrol() {

        // the reason it doesn't take a parameter is explained below
        // return the course it got from the Gym's list of courses

        // open a fragment to select the course to enrol
        // depending on the course u select, it will return to the previous page,
        // return an integer, in the case of selectCourseToLeave

        // PART 1: Makes a new fragment (screen)
        // ....

        // PART 2:

        // DELIVERABLE 3: Also, make sure the times aren't conflicting...

    }

    public void selectCourseToleave() {

        // remove the course here

    }

    /** Getter and Setter methods */
    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }


    public ArrayList<Course> getCoursesAttending() {
        return coursesAttending;
    }

    public void setCourseAttending(ArrayList<Course> coursesAttending) {
        this.coursesAttending = coursesAttending;
    }


}
