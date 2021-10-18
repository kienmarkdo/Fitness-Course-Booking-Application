package com.example.fitnesscoursebookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesscoursebookingapp.User;
import com.example.fitnesscoursebookingapp.GymManagement.Administrator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Welcome page / first page of the Fitness Course Booking Application
 *
 * Users can login with their username and password or create a new account
 */
public class MainActivity extends AppCompatActivity {

    // for reading and writing data to the firebase database
    private DatabaseReference localReference;

    // buttons and text fields
    Button moveToAdmin, login, createNewAccount;

    EditText usernameTextInput;
    EditText passwordTextInput;

    // list of possible activities this activity may navigate to
    private enum NextActivity {
        REGISTER_USER, ADMIN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.loginButton);
        createNewAccount = findViewById(R.id.registerButton);

        usernameTextInput = findViewById(R.id.usernameTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }

        });

        // TODO: onclick leads to a blank/white screen instead of leading to activity_register_user.xml
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(NextActivity.REGISTER_USER);
            }
        });
    } // end of onCreate

    /**
     * Performs all the necessary procedures while logging in.
     * Finds the inputted username in firebase, verifies the password.
     *  Displays error if username and password do not match or if username does not exist or
     *  proceeds to a new activity.
     */
    private void logIn() {

        String usernameInput = usernameTextInput.getText().toString();
        String passwordInput = passwordTextInput.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("username").equalTo(usernameInput);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    usernameTextInput.setError(null);

                    String databasePassword = dataSnapshot.child(usernameInput).child("password").getValue(String.class);

                    if (databasePassword.equals(passwordInput)) {
                        if (usernameInput.equals("admin")) {
                            switchActivities(NextActivity.ADMIN);
                        }
                    } else {
                        passwordTextInput.setError("Wrong password");
                        passwordTextInput.requestFocus();
                    }
                } else {
                    usernameTextInput.setError("No such username exists");
                    usernameTextInput.requestFocus();
                }
            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    } // end of login()

    /**
     * Reusable selector method for starting new activities
     * @param val name of the next activity represented with an enum class
     */
    private void switchActivities(NextActivity val) {

        Intent intent = new Intent();
        switch (val) {
            case ADMIN:
                intent = new Intent(this, activity_admin.class);
                break;
            case REGISTER_USER:
                intent = new Intent(this, activity_register_user.class);
                break;
        }
        startActivity(intent);

    } // end of switchActivities()


} // end of MainActivity