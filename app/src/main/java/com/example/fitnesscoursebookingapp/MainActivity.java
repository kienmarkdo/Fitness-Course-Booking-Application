package com.example.fitnesscoursebookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Welcome page / first page of the Fitness Course Booking Application
 *
 * Users can login with their username and password or create a new account
 */
public class MainActivity extends AppCompatActivity {

    // buttons and text fields
    Button moveToAdmin, login, createNewAccount;

    EditText usernameTextInput;
    EditText passwordTextInput;

    // list of possible activities this activity may navigate to
    private enum NextActivity {
        REGISTER_USER, ADMIN, WELCOME_SCREEN, INSTRUCTOR, GYMMEMBER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        login = findViewById(R.id.loginButton);
        createNewAccount = findViewById(R.id.registerButton);

        usernameTextInput = findViewById(R.id.usernameTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);

        updateUsers(db);
        // updateCourses(db);

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


        //Takes input from user
        String usernameInput = usernameTextInput.getText().toString();
        String passwordInput = passwordTextInput.getText().toString();

        //Fetches instance of database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        //Orders in search for the username
        Query checkUser = reference.orderByChild("username").equalTo(usernameInput);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Check if username input matches to existing name in database
                if (dataSnapshot.exists()) {

                    //Reset error tracking
                    usernameTextInput.setError(null);

                    //Moves the datasnapshot to reference user
                    DataSnapshot user = dataSnapshot.getChildren().iterator().next();

                    String databasePassword = user.child("password").getValue(String.class);

                    String role = user.child("userType").getValue(String.class);

                    //Checks if passowrd inputs matches to associatd passowrd with user.
                    if (passwordInput.equals(databasePassword)) {

                        //Reset error tracking
                        usernameTextInput.setError(null);

                        if (usernameInput.equals("admin")) {
                            switchActivities(NextActivity.ADMIN, null);
                        }

                        else if (role.equals("instructor")) {
                            String[] infoArr = {usernameInput};
                            switchActivities(NextActivity.INSTRUCTOR, infoArr);
                        }

                        else {
                            //String role = user.child("userType").getValue(String.class);
                            String[] infoArr = {role, usernameInput};
                            switchActivities(NextActivity.GYMMEMBER, infoArr);
                        }

                    }
                    else {
                        //Set error indicating user inputted password incorrectly
                        passwordTextInput.setError("Wrong password");
                        passwordTextInput.requestFocus();
                    }
                }
                else {
                    //Set error indicating user inputted username incorrectly
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
            case INSTRUCTOR:
                intent = new Intent(this, activity_instructor.class);
                intent.putExtra("strings", extra);
                break;
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
            case GYMMEMBER:
                intent = new Intent(this, activity_gym_member.class);
                intent.putExtra("strings", extra);
                break;

        }
        startActivity(intent);

    } // end of switchActivities()

    /**
     * Called when the app is opened. Initializes and keeps Gym updated throughout the app's
     * runtime.
     *
     * @param db is the FirebaseDatabase to fetch the data from.
     */
    private void updateUsers(FirebaseDatabase db) {
        Gym.listOfGymMember.clear();
        Gym.listOfInstructors.clear();

        // instantiate and actively update user information in Gym
        db.getReference("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addUser(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addUser(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                boolean isBothTypes = dataSnapshot.child("userType").getValue().equals("both");

                if (isBothTypes || dataSnapshot.child("userType").getValue().equals("gymMember")) {
                    Gym.listOfGymMember.remove(dataSnapshot.getKey());
                }
                if (isBothTypes || dataSnapshot.child("userType").getValue().equals("instructor")){
                    Gym.listOfInstructors.remove(dataSnapshot.getKey());
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    } // end of updateUsers()

    /**
     * Used in updateUsers() to add a new user to Gym.listOfGymMembers or Gy.listOfInstructors.
     *
     * @param user is the dataSnapshot that holds the users' information.
     */
    private void addUser(DataSnapshot user) {
        if (!user.getKey().equals("admin")) {
            boolean isBothTypes = user.child("userType").getValue(String.class).equals("both");

            if (isBothTypes || user.child("userType").getValue(String.class).equals("gymMember")) {
                Gym.listOfGymMember.put(user.getKey(), new GymMember(
                        user.child("username").getValue(String.class), user.child("password").getValue(String.class),
                        user.child("legalName").getValue(String.class), isBothTypes
                ));
            }
            if (isBothTypes || user.child("userType").getValue(String.class).equals("instructor")){
                Gym.listOfInstructors.put(user.getKey(), new Instructor(
                        user.child("username").getValue(String.class), user.child("password").getValue(String.class),
                        user.child("legalName").getValue(String.class), isBothTypes
                ));
            }
        } else {
            Gym.admin = new Administrator(
                    user.child("password").getValue(String.class), user.child("username").getValue(String.class)
            );
        }
    } // end of addUser()

    /**
     * Called when the app is opened. Initializes and keeps Gym updated with
     * the course information throughout the app's runtime.
     * @param db
     */
    private void updateCourses(FirebaseDatabase db) {
        Gym.listOfCourses.clear();

        // instantiate and actively update course information in Gym.
        db.getReference("Courses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Gym.listOfCourses.put(dataSnapshot.getKey(), new Course(
                    dataSnapshot.child("username").getValue(String.class),
                        dataSnapshot.child("description").getValue(String.class),
                        dataSnapshot.child("time").getValue(String.class),
                        dataSnapshot.child("hourDuration").getValue(Float.class),
                        Gym.listOfInstructors.get(dataSnapshot.child("teacher").getValue(String.class)),
                        dataSnapshot.child("experienceLevel").getValue(String.class),
                        dataSnapshot.child("capacity").getValue(Integer.class),
                        dataSnapshot.child("startTime").getValue(Float.class)
                ));
            }

            //TODO implement the changed and removed methods
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*for (DataSnapshot property : dataSnapshot.getChildren()) {

                }*/
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

} // end of MainActivity