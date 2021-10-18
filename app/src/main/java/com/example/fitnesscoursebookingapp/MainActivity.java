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


public class MainActivity extends AppCompatActivity {


    private DatabaseReference localReference;


    Button moveToAdmin, login, createNewAccount;

    EditText usernameTextInput;
    EditText passwordTextInput;

    private enum NextActivity {
        REGISTER_USER, ADMIN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = findViewById(R.id.loginButton);
        createNewAccount = findViewById(R.id.registerButton);


       usernameTextInput =  findViewById(R.id.usernameTextInput);
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
                NextActivity temp = NextActivity.REGISTER_USER;
                switchActivities(temp);
            }
        });
    }

    private void logIn() {

        String usernameInput= usernameTextInput.getText().toString();
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
                            NextActivity temp = NextActivity.ADMIN;
                            switchActivities(temp);
                        }
                    }

                    else {
                        passwordTextInput.setError("Wrong password");
                        passwordTextInput.requestFocus();
                    }
                }

                else {
                    usernameTextInput.setError("No such username exists");
                    usernameTextInput.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


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




    }


}