package com.example.fitnesscoursebookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;

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
        REGISTER_USER, ADMIN, WELCOME_SCREEN
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

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(NextActivity.REGISTER_USER, null);
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

        System.out.println(checkUser.toString());

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    System.out.println(dataSnapshot.toString());

                    usernameTextInput.setError(null);

                    boolean exists = dataSnapshot.child(usernameInput).exists();

                    System.out.println(exists);

                    DataSnapshot user = dataSnapshot.getChildren().iterator().next();


                    String databasePassword = user.child("password").getValue(String.class);

                    System.out.println(databasePassword);

                    if (passwordInput.equals(databasePassword)) {
                        if (usernameInput.equals("admin")) {
                            switchActivities(NextActivity.ADMIN, null);
                        }

                        else {
                            String role = user.child("userType").getValue(String.class);
                            String[] infoArr = {role, usernameInput};
                            switchActivities(NextActivity.WELCOME_SCREEN, infoArr);
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
    private void switchActivities(NextActivity val, String extra[]) {

        Intent intent = new Intent();
        switch (val) {
            case ADMIN:
                intent = new Intent(this, activity_admin.class);
                break;
            case REGISTER_USER:
                intent = new Intent(this, activity_register_user.class);
                break;
            case WELCOME_SCREEN:
                intent = new Intent(this, activity_welcome_screen.class);
                intent.putExtra("strings", extra);
                break;
        }
        startActivity(intent);

    } // end of switchActivities()


} // end of MainActivity