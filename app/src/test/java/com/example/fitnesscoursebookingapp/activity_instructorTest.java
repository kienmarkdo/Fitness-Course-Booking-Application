package com.example.fitnesscoursebookingapp;

import static org.junit.Assert.*;
import org.junit.Test;

//import com.google.firebase.firestore.util.Assert;

import org.junit.Test;

public class activity_instructorTest {

    /*@org.junit.Before
    public void setUp() throws Exception {
    }*/

    @Test
    public void testCapacityLimitValidate() {

        System.out.println("IN CAPACITY TESt");

        String test1 = "1";
        String test2 = "10";
        String test3 = "900";
        String test4 = "-1";

        assertFalse(activity_instructor.verifyValidCapacityLimit(test4));

    }

    @Test
    public void testVerifyDay() {

        System.out.println("In verify day");

        String test1 = "1";
        String test2 = "8";
        String test3 = "MON";
        String test4 = "TUES";
        String test5 = "TUESDAY";

        assertFalse(activity_instructor.verifyValidDay(test1));
        assertFalse(activity_instructor.verifyValidDay(test2));
        assertTrue(activity_instructor.verifyValidDay(test3));
        assertFalse(activity_instructor.verifyValidDay(test4));
        assertFalse(activity_instructor.verifyValidDay(test5));

    }


    @Test
    public void testVerifyStartTime() {
        System.out.println("In verify start time");

        String test1 = "1";
        String test2 = "8";
        String test3 = "1.5";
        String test4 = "TUES";
        String test5 = "Blarg";


        assertTrue(activity_instructor.verifyValidStartTime(test1));
        assertTrue(activity_instructor.verifyValidStartTime(test2));
        assertFalse(activity_instructor.verifyValidStartTime(test3));
        assertFalse(activity_instructor.verifyValidStartTime(test4));
        assertFalse(activity_instructor.verifyValidStartTime(test5));
    }

    @Test
    public void testVerifyDuration() {
        System.out.println("In verify duration");

        String test1 = "1";
        String test2 = "0.5";
        String test3 = "1.5";
        String test4 = "250";
        String test5 = "Blarg";

        assertTrue(activity_instructor.verifyDuration(test1));
        assertFalse(activity_instructor.verifyDuration(test2));
        assertTrue(activity_instructor.verifyDuration(test3));
        assertFalse(activity_instructor.verifyDuration(test4));
        assertFalse(activity_instructor.verifyDuration(test5));
    }

    @Test
    public void testVerifyName() {
        System.out.println("In verify name");

        String test1 = "1";
        String test2 = "";
        String test3 = "1.5";
        String test4 = "";
        String test5 = "Blarg";

        assertTrue(activity_instructor.verifyValidName(test1));
        assertFalse(activity_instructor.verifyValidName(test2));
        assertTrue(activity_instructor.verifyValidName(test3));
        assertFalse(activity_instructor.verifyValidName(test4));
        assertTrue(activity_instructor.verifyValidName(test5));

    }



    /*@org.junit.After
    public void tearDown() throws Exception {
    }*/
}