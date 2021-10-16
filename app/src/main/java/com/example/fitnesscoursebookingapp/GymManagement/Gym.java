package com.example.fitnesscoursebookingapp.GymManagement;

import com.example.fitnesscoursebookingapp.Course;
import com.example.fitnesscoursebookingapp.GymMember;
import com.example.fitnesscoursebookingapp.Instructor;

import java.util.ArrayList;


/**
 * A class that represents the Gym
 */
public class Gym {

   // intended to be default access modifier (only accessible by Administrator.java)
   ArrayList<Instructor> listOfInstructors;
   ArrayList<GymMember> listOfGymMember;
   ArrayList<Course> listOfCourses;

   // =================  Class methods  =================

   /**
    * Returns a Course by index
    * @param index Index of the course in the list
    * @return returns a Course
    */
   public Course getCourseAt(int index) {
      return listOfCourses.get(index);
   }


}
