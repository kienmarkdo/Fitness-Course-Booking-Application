package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Register new account page of the Fitness Course Booking Application
 *
 * The user enters this page if they do not have a username and password yet and would like to
 *  create one.
 */
public class activity_register_user extends Activity {

    // buttons and text fields
    Button login, createNewAccount;
    EditText usernameTextInput, passwordTextInput, confirmPasswordTextInput;
    RadioButton memberBtn, instructorBtn;
    RadioGroup selectAccountGroup;

    // list of possible activities this activity may navigate to
    private enum NextActivity {
        MAIN_ACTIVITY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // initialize buttons and text field ids
        login = findViewById(R.id.loginButton);
        createNewAccount = findViewById(R.id.registerButton);

        usernameTextInput = findViewById(R.id.usernameTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);
        confirmPasswordTextInput = findViewById(R.id.confirmPasswordTextInput);

        memberBtn = findViewById(R.id.memberBtn);
        instructorBtn = findViewById(R.id.instructorBtn);

        selectAccountGroup = findViewById(R.id.selectAccountTypeGroup);

        // TODO:
        //  **OPTIONAL**: Add a minimum requirement for username and password (ie: password must be at least 8 characters long)

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAllUserInput();
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
     * Verifies the following,
     * - All buttons and text fields are filled in (no text fields are left empty, one of the Select Account Type buttons are selected)
     * - The user inputted username does not exist in the database and is therefore a valid username
     * - The password and the confirm password text fields match
     * - OPTIONAL: Verify if username and password meet minimum requirements (i.e.: must be at least 8 characters)
     * <p>
     * NOTE: Each of the bullet points above have their own methods
     * TODO: Should the new user be able to add their legal names upon creating a new account? We know they will be able to do that
     *  in their own settings menu after logging in, but should they be able to write their legal names upon creating a new account or not?
     */
    private void verifyAllUserInput() {

        if (allFieldsFilledIn() && passwordsMatch()) {
            addNewAccount();
        }

    } // end of verifyUserInput()

    /**
     * Verifies whether all buttons and text fields are filled in or not
     * That is, no text fields are left empty AND one of the Select Account Type buttons are selected
     *
     * @return true if all fields are filled in, false otherwise
     */
    private boolean allFieldsFilledIn() {

        boolean isAllFilledIn = true;

        if (TextUtils.isEmpty(usernameTextInput.getText().toString())) {
            usernameTextInput.setError("Username cannot be empty.");
            isAllFilledIn = false;
        }
        if (TextUtils.isEmpty(passwordTextInput.getText().toString())) {
            passwordTextInput.setError("Password cannot be empty.");
            isAllFilledIn = false;
        }
        if (TextUtils.isEmpty(confirmPasswordTextInput.getText().toString())) {
            confirmPasswordTextInput.setError("Confirm password cannot be empty.");
            isAllFilledIn = false;
        }
        if (!instructorBtn.isChecked() && !memberBtn.isChecked()) {
            memberBtn.setError("Please select an account type.");
            instructorBtn.setError("Please select an account type.");
            isAllFilledIn = false;
        }

        return isAllFilledIn;
    } // end of allFieldsFilledIn()


    /**
     * Checks to see if the password and confirm passwords entered by the user match or not
     *
     * @return true if the passwords match
     */
    private boolean passwordsMatch() {

        String password = passwordTextInput.getText().toString();
        String confirmPassword = confirmPasswordTextInput.getText().toString();
        boolean isMatch = password.equals(confirmPassword);

        if (!isMatch) {
            passwordTextInput.getText().clear();
            confirmPasswordTextInput.getText().clear();
            passwordTextInput.requestFocus();
            passwordTextInput.setError("Passwords do not match.");
        } else {
            passwordTextInput.setError(null);
        }

        return isMatch;
    } // end of passwordsMatch()

    /**
     * Registers the new account into firebase
     *
     * This method includes the following functionalities:
     *  - Checking to see whether the username already exists or not and prompts the user to re-enter a new username if true
     *  - Adds the new username and password into the database
     */
    private void addNewAccount() {

        // TODO: Currently, we do not convert the user-inputted username to lowercase/uniformcase.
        //  Therefore, the usernames "Bob1234" and "bob1234" can co-exist.

        String usernameInputStr = usernameTextInput.getText().toString();
        String passwordInputStr = passwordTextInput.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("username").equalTo(usernameInputStr);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // checks whether the username already exists or not
                if (dataSnapshot.exists()) {
                    usernameTextInput.setError("This username already exists. Please re-enter a new username.");
                    usernameTextInput.requestFocus();
                } else {
                    usernameTextInput.setError(null);

                    // account type is gym member
                    if (memberBtn.isChecked() && !instructorBtn.isChecked()) {
                        User newMember = new GymMember(usernameInputStr, passwordInputStr);
                        reference.push().setValue(newMember); // add the new Gym Member here
                        printUserAddedSuccessMessage();
                    }
                    // account type is instructor
                    else if (!memberBtn.isChecked() && instructorBtn.isChecked()) {
                        User newInstructor = new Instructor(usernameInputStr, passwordInputStr);
                        reference.push().setValue(newInstructor); // add the new Gym Member here
                        printUserAddedSuccessMessage();
                    }

                } // end of outer if/else

            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of checkUser listener
    } // end of usernameAlreadyExists()

    /**
     * Displays a small pop-up at the bottom of the screen indicating that registering the user was a success
     */
    private void printUserAddedSuccessMessage() {

        // hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        usernameTextInput.getText().clear();
        passwordTextInput.getText().clear();
        confirmPasswordTextInput.getText().clear();
        selectAccountGroup.clearCheck();

        // displays the success message
        Context context = getApplicationContext();
        CharSequence text = "Account Registered Successfully!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    } // end of printUserAddedSuccessMessage()


    /**
     * Reusable selector method for starting new activities
     *
     * @param val name of the next activity represented with an enum class
     */
    private void switchActivities(NextActivity val) {

        Intent intent = new Intent();
        switch (val) {
            case MAIN_ACTIVITY:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        startActivity(intent);

    } // end of switchActivities()


} // end of activity_register_user
