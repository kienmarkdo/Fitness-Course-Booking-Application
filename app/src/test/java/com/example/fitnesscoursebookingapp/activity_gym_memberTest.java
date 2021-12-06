package com.example.fitnesscoursebookingapp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class activity_gym_memberTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testValidateEnrollDay(){

        String s1 = "WED";
        String s2 = "TUE";
        String s3 = "FRI";
        Instructor t1 = new Instructor("vb", "bc", "bobby");
        Course course1 = new Course("Swimming", "swim");
        course1.setTime("MON");
        Course course2 = new Course("Rugby", "run into people");
        course2.setTime("TUE");
        Course course3 = new Course("Running", "go fast");
        course2.setTime("WED");


        activity_gym_member.enrolledList.add(course1);
        activity_gym_member.enrolledList.add(course2);
        activity_gym_member.enrolledList.add(course3);

        assertTrue(activity_gym_member.validateEnrollDay(s1));
        assertTrue(activity_gym_member.validateEnrollDay(s2));
        assertFalse(activity_gym_member.validateEnrollDay(s3));
    }

    @Test
    public void testValidateDayInput(){
        //MON, TUE, WED, THU, FRI

        String s1 = "MON";
        String s2 = "FRI";
        String s3 = "kvjv";
        String s4 = "tue";

        assertTrue(activity_gym_member.validateDayInput(s1));
        assertTrue(activity_gym_member.validateDayInput(s2));
        assertFalse(activity_gym_member.validateDayInput(s3));
        assertFalse(activity_gym_member.validateDayInput(s4));
    }

    @Test
    public void testValidateEnrollWithinCapacity(){
       // activity_gym_member g1 = new activity_gym_member();


    }
}