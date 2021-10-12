package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_admin extends Activity {

    Button createClass,editClass, deleteClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        createClass = findViewById(R.id.createClass);
        editClass = findViewById(R.id.editClass);
        deleteClass = findViewById(R.id.deleteClass);


        /*
        createClass.setOnClickListener( {
            public void onClick(View v) {

            }
        });

        editClass.setOnClickListener( {
            public void onClick(View v) {

            }
        });

        deleteClass.setOnClickListener( {
            public void onClick(View v) {

            }
        });

        */


    }


}
