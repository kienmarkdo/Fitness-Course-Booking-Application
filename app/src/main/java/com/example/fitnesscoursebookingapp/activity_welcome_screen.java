package com.example.fitnesscoursebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class activity_welcome_screen extends AppCompatActivity {


    TextView welcome, userRole, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Simply displays user information
        //Strings passed through intent are created within
        //main activity under the appropraite sign in.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        welcome = findViewById(R.id.welcomeScreenTitle);
        userRole = findViewById(R.id.userRole);
        userName = findViewById(R.id.userName);

        Intent intent = getIntent();
        String[] localStrings = intent.getStringArrayExtra("strings");

        userRole.setText(localStrings[0]);
        userName.setText(localStrings[1]);
    }
}