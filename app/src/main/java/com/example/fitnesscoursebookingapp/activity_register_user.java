package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class activity_register_user extends Activity {

    // buttons and text fields
    Button login, createNewAccount;
    EditText usernameTextInput, passwordTextInput, confirmPasswordTextInput;

    // list of possible activities this activity may navigate to
    private enum NextActivity {
        MAIN_ACTIVITY
    }

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

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUserInput();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(NextActivity.MAIN_ACTIVITY);
            }
        });

    } // end of onCreate()

    // =============================   HELPER METHODS   =============================

    /**
     * Verifies the following
     * - All buttons and text fields are filled in (no text fields are left empty, one of the Select Account Type buttons are selected)
     * - The user inputted username does not exist in the database and is therefore a valid username
     * - The password and the confirm password text fields match
     * - OPTIONAL: Verify if username and password meet minimum requirements (i.e.: must be at least 8 characters)
     *
     * TODO: Should the new user be able to add their legal names upon creating a new account? We know they will be able to do that
     *  in their own settings menu after logging in, but should they be able to write their legal names upon creating a new account or not?
     */
    private void verifyUserInput() {
        // check if all text fields and buttons are filled in

        // go through firebase to see if this user already exists or not
            // if yes, display error to user telling them to type in a new username
            // if no, proceed with valid steps

        // check to see if password fields match or not

        // show success screen and/or automatically jump to the login screen
    } // end of verifyUserInput()


    /**
     * Reusable selector method for starting new activities
     * @param val name of the next activity represented with an enum class
     */
    private void switchActivities(activity_register_user.NextActivity val) {

        Intent intent = new Intent();
        switch (val) {
            case MAIN_ACTIVITY:
                intent = new Intent(this, activity_admin.class);
                break;
        }
        startActivity(intent);

    } // end of switchActivities()



} // end of activity_register_user
