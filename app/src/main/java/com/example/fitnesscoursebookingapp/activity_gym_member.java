package com.example.fitnesscoursebookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class activity_gym_member extends AppCompatActivity {

    TextView courseName;
    TextView weekDay;

    EditText editCourseName;
    EditText editDay;

    Button enrollButton;
    Button unenrollButton;
    Button listEnrolledButton;

    ListView listViewCourses;

    List<Course> courseList;

    List<Course> enrolledList;

    DatabaseReference databaseCourses;

    static String[] dayStrings = {"MON", "TUE", "WED", "THU", "FRI"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member);

        courseName = (TextView) findViewById(R.id.courseNameView);
        weekDay = (TextView) findViewById(R.id.weekDayView);

        editCourseName = (EditText) findViewById(R.id.editClassName);
        editDay = (EditText) findViewById(R.id.editWeekDay);

        enrollButton = (Button) findViewById(R.id.enrollButton);
        unenrollButton = (Button) findViewById(R.id.unenrollButton);
        listEnrolledButton= (Button) findViewById(R.id.listEnrolledButton);

        courseList = new ArrayList<>();
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");

        listViewCourses= findViewById(R.id.courseList);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrollCourse();
            }
        });

        unenrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeHolder();
            }
        });

        listEnrolledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeHolder();
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
                CourseList courseAdapter = new CourseList(activity_gym_member.this, courseList);
                listViewCourses.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void placeHolder() {
        System.out.println("aplce");
    }

    public void enrollCourse() {
        //clearErrors();

        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();


        /*boolean problem = false;
        } if (!verifyValidDay(day)) {
            editDay.setError("Must be a valid day");
            editDay.requestFocus();
            problem = true;
        } if (!verifyValidName(courseName)) {
            editCourseName.setError("Must set a name");
            editCourseName.requestFocus();
            problem = true;
        }
        if (problem) {
            return;
        }*/

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

                            enrolledList.add(tempCourse);
                        }
                    }

                    // no course was found during the specified day
                    editDay.setError("There is no class to cancel on this day");
                    editDay.requestFocus();


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


    /*private void clearErrors() {
        editCourseName.setError(null);
        editWeekDay.setError(null);
    }*/
}