package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class activity_admin extends Activity {

    Button createCourse, editCourse, deleteCourse, deleteUser;

    //brainstorming
    EditText courseNameTextInput;
    EditText courseDescriptionTextInput;
    EditText accountInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //This code will have comments removed once the admin frontEnd has been implemented
        //brainstorming
        //


        createCourse = findViewById(R.id.createCourse);
        editCourse = findViewById(R.id.editCourse);
        deleteCourse = findViewById(R.id.deleteCourse);
        deleteUser = findViewById(R.id.deleteUser);

        courseNameTextInput = findViewById(R.id.courseNameTextInput); // for adding / deleting courses
        courseDescriptionTextInput = findViewById(R.id.courseDescriptionTextInput); // for adding a description
        accountInput = findViewById(R.id.usernameTextInput); // for deleting a user account
        /*
        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { createCourse(); }
        });

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { editCourse(); }
        });

        deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { deleteCourse(); }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { deleteAccount(); }
        });

         */
        TextView success = findViewById(R.id.loginSuccessfull);

    }

    //commented out methods to add in once front end is complete
    //may be separate depending on how front-end is implemented
    private void createCourse() {

    }

    private void editCourse() {

    }

    private void deleteCourse() {

    }

    private void deleteAccount() {

    }

}