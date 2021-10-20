package com.example.fitnesscoursebookingapp;

import java.util.HashMap;


/**
 * A class that represents the Gym
 */
public class Gym {

   // intended to be default access modifier (only accessible by Administrator.java)
   static HashMap<String, Instructor> listOfInstructors;
   static HashMap<String, GymMember> listOfGymMember;
   static HashMap<String, Course> listOfCourses;

   // =================  Class methods  =================

   /**
    * Returns a Course by index
    * @param index Index of the course in the list
    * @return returns a Course
    */
   public Course getCourseAt(String index) {
      return listOfCourses.get(index);
   }


}
