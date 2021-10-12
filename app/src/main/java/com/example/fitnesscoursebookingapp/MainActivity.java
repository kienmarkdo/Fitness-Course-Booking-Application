package com.example.fitnesscoursebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;



public class MainActivity extends AppCompatActivity {

    Button moveToAdmin, login, createNewAccount;


    private enum NextActivity {
        REGISTER_USER, ADMIN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = findViewById(R.id.loginButton);
        createNewAccount = findViewById(R.id.registerButton);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextActivity temp = NextActivity.ADMIN;
                switchActivities(temp);
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