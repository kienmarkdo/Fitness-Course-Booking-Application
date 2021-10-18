package com.example.fitnesscoursebookingapp;

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

    Administrator admin = new Administrator();


    private enum NextActivity {
        REGISTER_USER, ADMIN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = findViewById(R.id.loginButton);
        createNewAccount = findViewById(R.id.registerButton);


        EditText usernameTextInput = findViewById(R.id.usernameTextInput);
        EditText passwordTextInput = findViewById(R.id.passwordTextInput);

        String usernameInput= usernameTextInput.getText().toString();
        String passwordInput = passwordTextInput.getText().toString();


        FirebaseDatabase database= FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("Users").child("admin");



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        admin = dataSnapshot.getValue(Administrator.class);
                        Log.d("userNametest", admin.getUsername());
                        Log.d("passwordTest", admin.getPassword());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }



                };

                String tempPassword = admin.getPassword();

                Toast toast = Toast.makeText(getApplicationContext(), tempPassword, Toast.LENGTH_SHORT);
                toast.show();



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