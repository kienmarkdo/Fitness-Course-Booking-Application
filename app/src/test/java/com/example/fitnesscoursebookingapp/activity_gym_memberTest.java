package com.example.fitnesscoursebookingapp;

import static org.junit.Assert.*;
import org.junit.Test;

public class activity_gym_memberTest {


    @Test
    public void testVerifyDayEqualsTrue(){
        Course c1 = new Course("Swimming", "You swim", "MON", 0,
                null, "Beginner", 30, 0);
        Course c2 = new Course("Running", "You run", "WED", 0,
                null, "Beginner", 15, 0);

        assertTrue(activity_gym_member.verifyDayEquals(c1,"MON"));
        assertTrue(activity_gym_member.verifyDayEquals(c2,"WED"));

        c1.setTime("FRI");
        c2.setTime("THU");

        assertTrue(activity_gym_member.verifyDayEquals(c1,"FRI"));
        assertTrue(activity_gym_member.verifyDayEquals(c2,"THU"));


    }

    @Test
    public void testVerifyDayEqualsFalse(){
        Course c1 = new Course("Swimming", "You swim", "MON", 0,
                null, "Beginner", 30, 0);
        Course c2 = new Course("Running", "You run", "WED", 0,
                null, "Beginner", 15, 0);

        assertFalse(activity_gym_member.verifyDayEquals(c1,"FRI"));
        assertFalse(activity_gym_member.verifyDayEquals(c2,"TUE"));

        c1.setTime("FRI");
        c2.setTime("TUE");

        assertFalse(activity_gym_member.verifyDayEquals(c1,"WED"));
        assertFalse(activity_gym_member.verifyDayEquals(c2,"THU"));


    }

    @Test
    public void testValidateDayInputIsTrue(){
        //MON, TUE, WED, THU, FRI

        String s1 = "MON";
        String s2 = "FRI";
        String s3 = "TUE";
        String s4 = "WED";
        String s5 = "THU";

        assertTrue(activity_gym_member.validateDayInput(s1));
        assertTrue(activity_gym_member.validateDayInput(s2));
        assertTrue(activity_gym_member.validateDayInput(s3));
        assertTrue(activity_gym_member.validateDayInput(s4));
        assertTrue(activity_gym_member.validateDayInput(s5));
    }

    @Test
    public void testValidateDayInputIsFalse(){
        //MON, TUE, WED, THU, FRI

        String s1 = "kiwi";
        String s2 = "tue";
        String s3 = "final";
        String s4 = "";

        assertFalse(activity_gym_member.validateDayInput(s1));
        assertFalse(activity_gym_member.validateDayInput(s2));
        assertFalse(activity_gym_member.validateDayInput(s3));
        assertFalse(activity_gym_member.validateDayInput(s4));
    }

    @Test
    public void testValidateEnrollWithinCapacityIsTrue(){
        Course c1 = new Course("Swimming", "You swim", "MON", 0,
        null, "Beginner", 30, 0);


        c1.setStudentAmount(15);
        assertTrue(activity_gym_member.validateEnrollWithinCapacity(c1));
        c1.setStudentAmount(29);
        assertTrue(activity_gym_member.validateEnrollWithinCapacity(c1));
        c1.setStudentAmount(1);
        assertTrue(activity_gym_member.validateEnrollWithinCapacity(c1));
        c1.setStudentAmount(20);
        assertTrue(activity_gym_member.validateEnrollWithinCapacity(c1));

    }

    @Test
    public void testValidateEnrollWithinCapacityIsFalse(){
        Course c1 = new Course("Swimming", "You swim", "MON", 0,
                null, "Beginner", 30, 0);
        Course c2 = new Course("Running", "You run", "WED", 0,
                null, "Beginner", 15, 0);

        c1.setStudentAmount(30);
        assertFalse(activity_gym_member.validateEnrollWithinCapacity(c1));
        c2.setStudentAmount(15);
        assertFalse(activity_gym_member.validateEnrollWithinCapacity(c2));

    }
}