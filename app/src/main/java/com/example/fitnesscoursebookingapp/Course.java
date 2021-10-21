package com.example.fitnesscoursebookingapp;

import java.util.ArrayList;

/**
 * A course in the gym
 *
 * Example: HIIT, Tennis, Badminton, etc.
 */
public class Course {

    /** Relevant variables about a course */
    private Instructor teacher;
    private String name;
    private String description;
    private String experienceLevel; // indicates the experience a student should have before enrolling (i.e.: beginner, expert...)
    private String time; // hour, date, month, year
    private float hourDuration; // how long the course lasts (i.e.: 1.5 hours)
    private int numberOfStudents; // number of students enrolled in a course
    private ArrayList<GymMember> students; // list of all students (this stays as an array because there is a max num of students)

    /** Constructor methods */
    public Course() {
        // set size of the array GymMember here; that indicates the max amount of students a course can have
        numberOfStudents = 0;
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description, String time, float hourDuration,
                  Instructor teacher, String experienceLevel, ArrayList<GymMember> students) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.hourDuration = hourDuration;
        this.teacher = teacher;
        this.experienceLevel = experienceLevel;
        this.students = students;
    }

    /** Get and Set methods */
    public Instructor getTeacher() {
        return teacher;
    }

    public void setTeacher(Instructor teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getHourDuration() {
        return hourDuration;
    }

    public void setHourDuration(float hourDuration) {
        this.hourDuration = hourDuration;
    }

    public int getStudentAmount() {
        return numberOfStudents;
    }

    public void setStudentAmount(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public ArrayList<GymMember> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<GymMember> students) { this.students = students; }

} // end of Course class
