package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_instructor extends Activity {


    String instructorId;

    TextView courseName;
    TextView experience;
    TextView day;
    TextView capacityLimit;
    TextView duration;

    EditText editCourseName;
    EditText editExperience;
    EditText editDay;
    EditText editCapacityLimit;
    EditText editDuration;

    Button createCourseButton;
    Button editCourseButton;
    Button cancelCourseButton;

    ListView listViewCourses;

    List<Course> courseList;

    DatabaseReference databaseCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String[] localStrings = intent.getStringArrayExtra("strings");

        instructorId = localStrings[0];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        TextView textViewName = (TextView) findViewById(R.id.courseNameView);
        TextView textViewExperience = (TextView) findViewById(R.id.experience);
        TextView textViewDay = (TextView) findViewById(R.id.dayView);
        TextView textViewCapacity = (TextView) findViewById(R.id.capacityLimit);
        TextView textViewDuration = (TextView) findViewById(R.id.durationView);

        editCourseName = (EditText) findViewById(R.id.editCourseName);
        editExperience = (EditText) findViewById(R.id.editExperience);
        editDay= (EditText) findViewById(R.id.editDay);
        editCapacityLimit = (EditText) findViewById(R.id.editCapacityLimit);
        editDuration= (EditText) findViewById(R.id.editDuration);

        createCourseButton = (Button) findViewById(R.id.createCourse);
        cancelCourseButton = (Button) findViewById(R.id.cancelButton);
        editCourseButton = (Button) findViewById(R.id.editCourse);

        courseList = new ArrayList<>();
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");

        listViewCourses= findViewById(R.id.courseList);

        createCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCourse();
            }
        });

        cancelCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCourse();
            }
        });

        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCourse();
            }
        });


    }


    protected void onStart() {
        super.onStart();

        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    Course tempCourse = postSnapshot.getValue(Course.class);
                    courseList.add(tempCourse);
                }
                CourseList courseAdapter = new CourseList(activity_instructor.this, courseList);
                listViewCourses.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void checkSchedulingConflict() {

    }


    public void createCourse() {

        // TODO: Error trap to see if the user inputted a valid DAY OF THE WEEK or not
        //  this way, we can error trap from a list of 7 abbreviated strings (MON, TUES, WED...)


        String courseName = editCourseName.getText().toString();
        String experience = editExperience.getText().toString();
        String day = editDay.getText().toString();
        String capacityLimit = editCapacityLimit.getText().toString();
        String duration = editDuration.getText().toString();

        boolean check1 = courseName.equals("");
        boolean check2 = experience.equals("");
        boolean check3 = day.equals("");
        boolean check4 = capacityLimit.equals("");
        boolean check5 = duration.equals("");

        // =======  check if the course name is empty or not  =======
        /*if (!(check1 && check2 && check3 && check4 && check5)) {
            editCourseName.setError("Cannot contain empty field");
            editCourseName.requestFocus();
            return;
        }*/

        // NOTE: The input is case sensitive, which means the course "Tennis" and "tennis" may co-exist at the same time

        // =======  check if the course exists in the database or not  =======

        // fetches instance of database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        // Orders in search for the course name
        Query checkCourse = reference.orderByChild("name").equalTo(courseName);



        checkCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Course tempCourse = postSnapshot.getValue(Course.class);

                        if (tempCourse.getTime().equals(day)) {
                            editCourseName.setError("Day already scheduled");
                            editCourseName.requestFocus();
                            return;
                        }
                    }

                    editCourseName.setError(null);
                    editCourseName.setError(null);
                    DatabaseReference ref = reference.push(); // add new course here
                    ref.setValue(new Course(courseName, "placeholder", day, Float.parseFloat(duration), new Instructor(instructorId), experience));
                    editCourseName.setText("");
                    editExperience.setText("");
                    editDay.setText("");
                    editCapacityLimit.setText("");
                    editDuration.setText("");

                } else {
                    editCourseName.setError("Class type does not exist");
                    editCourseName.requestFocus();
                } // end of outer if/else

            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of checkCourse listener

    }

    public void cancelCourse() {

        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();


        boolean check1 = courseName.equals("");

        boolean check3 = day.equals("");

        // =======  check if the course name is empty or not  =======
        /*if (!(check1 && check2 && check3 && check4 && check5)) {
            editCourseName.setError("Cannot contain empty field");
            editCourseName.requestFocus();
            return;
        }*/

        // NOTE: The input is case sensitive, which means the course "Tennis" and "tennis" may co-exist at the same time

        // =======  check if the course exists in the database or not  =======

        // fetches instance of database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        // Orders in search for the course name
        Query checkCourse = reference.orderByChild("name").equalTo(courseName);



        checkCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Course tempCourse = postSnapshot.getValue(Course.class);

                        if (tempCourse.getTime().equals(day)) {
                            editCourseName.setError(null);
                            editCourseName.setError(null);
                            postSnapshot.getRef().removeValue();
                            editCourseName.setText("");
                            editDay.setText("");
                            return;
                        }
                    }




                } else {
                    editCourseName.setError("Class does not exist");
                    editCourseName.requestFocus();
                } // end of outer if/else

            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of checkCourse listener

    }

    /**
     * Edit a course that this instructor currently teaches (a course that is within this java class's courseList).
     */
    public void editCourse() {


    }

    /**
     * Searches and displays the courses found on the database.
     * Can display all courses taught by the following search criterias
     *  - Instructor name (i.e.: display all courses taught by John Doe)
     *  - Course type (i.e.: display all Basketball courses)
     *  - Both of the above (i.e.: display all Judo courses taught by John Doe)
     */
    public void searchCourse() {

        // TODO: Add a search course button
        //  once the user clicks on search (assume no errors), change the text in the "Search" button
        //  to "Reset". Clear all text fields and redisplay all courses again.
    }
}