package com.example.fitnesscoursebookingapp;

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
    private String courseType; // name of the course type (i.e.: HIIT, Tennis, Badminton...)
    private String time; // hour, date, month, year
    private float hourDuration; // how long the course lasts (i.e.: 1.5 hours)
    private int courseID; // a unique identifier for a course - useful for database organization
    private int numberOfStudents; // number of students enrolled in a course
    private GymMember[] students; // list of all students (this stays as an array because there is a max num of students)

    /** Constructor methods */
    public Course() {
        // set size of the array GymMember here; that indicates the max amount of students a course can have
        numberOfStudents = 0;
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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
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

    public GymMember[] getStudents() {
        return students;
    }

    public void setStudents(GymMember[] students) {
        this.students = students;
    }

} // end of Course class