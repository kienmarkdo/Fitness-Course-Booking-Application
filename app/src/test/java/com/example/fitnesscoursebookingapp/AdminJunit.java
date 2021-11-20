package com.example.fitnesscoursebookingapp;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class AdminJunit {

    private activity_admin adminActivity;

    @Before
    public void setUp(){
        adminActivity = new activity_admin();
    }

    @Test
    public void checkCreateCourseNameEmpty(){
       /*
       adminActivity.addCourseInput.setText("");
       adminActivity.addCourseDescriptionInput.setText("kdhvdkjvhad");
       adminActivity.createCourseBtn.performClick();
       assertSame("The course name cannot be blank.", adminActivity.addCourseInput.getError());
       */


    }

    @Test
    public void testDeleteCourse(){

    }

    @Test
    public void testEditCourse(){

    }



    @Test
    public void testDeleteUserIfExist(){

    }
}
