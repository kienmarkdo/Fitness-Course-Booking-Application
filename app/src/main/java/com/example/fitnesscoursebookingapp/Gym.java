package com.example.fitnesscoursebookingapp;

import java.util.HashMap;


/**
 * A class that represents the Gym
 */
public class Gym {

   // static for use between all activities
   static int numberOfUsers;
   static HashMap<String, Instructor> listOfInstructors = new HashMap<String, Instructor>();
   static HashMap<String, GymMember> listOfGymMember = new HashMap<String, GymMember>();
   static HashMap<String, Course> listOfCourses = new HashMap<String, Course>();
   static Administrator admin;

}