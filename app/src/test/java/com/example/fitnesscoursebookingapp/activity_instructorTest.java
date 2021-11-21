package com.example.fitnesscoursebookingapp;

import static org.junit.Assert.*;

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

        assertTrue(activity_instructor.verifyValidCapacityLimit(test1));
        assertTrue(activity_instructor.verifyValidCapacityLimit(test2));
        assertTrue(activity_instructor.verifyValidCapacityLimit(test3));
        assertFalse(activity_instructor.verifyValidCapacityLimit(test4));

    }

    /*@org.junit.After
    public void tearDown() throws Exception {
    }*/
}