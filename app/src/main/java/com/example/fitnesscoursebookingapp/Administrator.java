package com.example.fitnesscoursebookingapp;

/**
 * The administrator manages all possible types of classes that
 * can be offered to members at the fitness centre.
 */
public class Administrator extends User {

    public Administrator() {
    }

    /** Constructor */
    public Administrator(String password, String username) {
        super(username, password);
        usertype = "admin";
    }

    /** Class Methods */

    // ========  course types management  ========

    // TODO: Pending; verifying the meaning of "Course Type".
    //  Does it mean to remove an ENTIRE TYPE OF COURSES (remove SEG2105 from the list of courses that this gym offers)
    //  or does it mean DELETE AN INSTANTIATED COURSE?
    //  EXAMPLE: SEG2105 has sections A and B. Are we removing JUST section A, or are we removing the entirety of SEG2105?
    //  (remove all instructors and students from all existing courses, then delete all existing courses, then remove
    //  the course from the list of courses this gym offers)
    public void createCourseType(){

    } // end of createCourseType()

    public void editCourseType(){

    } // end of editCourseType()

    public void deleteCourseType(){

    } // end of deleteCourseType()

    // ========  user management  ========

    public void deleteUserAccount(){

    } // end of deleteUserAccount()

} // end of Administrator class
