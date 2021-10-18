package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class activity_register_user extends Activity {

    // buttons and text fields
    Button login, createNewAccount;
    EditText usernameTextInput, passwordTextInput, confirmPasswordTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // initialize buttons and text field ids
        login = findViewById(R.id.loginButton);
        createNewAccount = findViewById(R.id.registerButton);

        usernameTextInput = findViewById(R.id.usernameTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);
        confirmPasswordTextInput = findViewById(R.id.confirmPasswordTextInput);

        // TODO: Write the methods for checking if the new username exists or not,
        //  if the new username does not already exist, add the new username and password to the database
        //  **OPTIONAL**: Add a minimum requirement for username and password (ie: password must be at least 8 characters long)

    } // end of onCreate()



} // end of activity_register_user
