package com.example.fitnesscoursebookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.lang.reflect.Array;
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
    Button searchButton;

    ListView listViewCourses;

    List<Course> courseList;

    List<Course> enrolledList;

    String activeUser;

    DatabaseReference databaseCourses;

    DatabaseReference databaseUsers;

    Query checkUser;



    static String[] dayStrings = {"MON", "TUE", "WED", "THU", "FRI"};

    int listEnrolledState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member);

        Intent intent = getIntent();
        String[] localStrings = intent.getStringArrayExtra("strings");
        activeUser = localStrings[1];

        courseName = (TextView) findViewById(R.id.courseNameView);
        weekDay = (TextView) findViewById(R.id.weekDayView);

        editCourseName = (EditText) findViewById(R.id.editClassName);
        editDay = (EditText) findViewById(R.id.editWeekDay);

        enrollButton = (Button) findViewById(R.id.enrollButton);
        unenrollButton = (Button) findViewById(R.id.unenrollButton);
        listEnrolledButton= (Button) findViewById(R.id.listEnrolledButton);
        searchButton = (Button) findViewById(R.id.searchCourseButton);

        courseList = new ArrayList<>();
        enrolledList = new ArrayList<>();
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");
        //currentUser = FirebaseDatabase.getInstance().getReference("")

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        // Orders in search for the course name
        checkUser = databaseUsers.orderByChild("username").equalTo(activeUser);

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
                unenroll();
            }
        });

        listEnrolledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listEnrolledCourses();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCourse();
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

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enrolledList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    GymMember tempMember= postSnapshot.getValue(GymMember.class);
                    if (tempMember.getUsername().equals(activeUser)) {
                        ArrayList<Course> tempEnrolledList = tempMember.getCoursesAttending();
                        System.out.println("List retrieved is " + tempEnrolledList);
                        for (int i = 0; i < tempEnrolledList.size(); i++) {
                            System.out.println("Adding course " + tempEnrolledList.get(i).getName());
                            enrolledList.add(tempEnrolledList.get(i));
                        }
                    }
                }
                CourseList courseAdapter = new CourseList(activity_gym_member.this, courseList);
                listViewCourses.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    public void unenroll() {
        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();

        for (int i = 0; i < enrolledList.size(); i++) {
            Course temp = enrolledList.get(i);
            if (courseName.equals(temp.getName()) && day.equals(temp.getTime())) {
                enrolledList.remove(i);
                pushListToDataBase();
            }
        }
    }

    public void placeHolder() {
        System.out.println("aplce");
    }

    public void pushListToDataBase() {
        System.out.println("Entered pushlist");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = userRef.orderByChild("username").equalTo(activeUser);
        System.out.println("After checkuser");
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //enrolledList.clear();

                System.out.println("Entered on data change");

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    GymMember tempMember= postSnapshot.getValue(GymMember.class);
                    System.out.println("User while scanning : " + tempMember.getUsername());
                    if (tempMember.getUsername().equals(activeUser)) {
                        DatabaseReference user = dataSnapshot.getChildren().iterator().next().getRef();
                        System.out.println("Found user " + tempMember.getUsername());
                        user.child("coursesAttending").setValue(enrolledList);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void enrollCourse() {
        //clearErrors();



        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();

        for (int i = 0; i < enrolledList.size(); i++) {
            Course temp = enrolledList.get(i);
            if (courseName.equals(temp.getName()) && day.equals(temp.getTime())) {
                editCourseName.setError("Already enrolled in this class");
                return;
            }
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Courses");
        Query checkCourse = userRef.orderByChild("name").equalTo(courseName);

        checkCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    System.out.println("Now scanning for correct course to enroll");

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Course tempCourse = postSnapshot.getValue(Course.class);

                        System.out.println("Current coures scanning is " + tempCourse.getName());

                        if (tempCourse.getTime().equals(day)) {

                            enrolledList.add(tempCourse);
                            pushListToDataBase();
                            return;
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

    public void listEnrolledCourses() {

        System.out.println(enrolledList);

        if (listEnrolledState == 0) {
            System.out.println("In state zero dispalying enrolled");
            CourseList courseAdapter = new CourseList(activity_gym_member.this, enrolledList);
            listViewCourses.setAdapter(courseAdapter);
            listEnrolledState = 1;
        }

        else {
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
            listEnrolledState = 0;
        }




    }

    public void searchCourse() {
        //clearErrors();

        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();

        boolean courseNameEmpty = courseName.equals("");
        boolean dayEmpty = day.equals("");

        if (courseNameEmpty && dayEmpty) {
            databaseCourses.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    courseList.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

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

            return;
        }


        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Course tempCourse = postSnapshot.getValue(Course.class);

                    boolean test1 = tempCourse.getTime().equals(day);
                    boolean test2 = tempCourse.getName().equals(courseName);

                    System.out.println("test1: " + test1);
                    System.out.println("test2: " + test2);

                    if (courseNameEmpty && !dayEmpty) {
                        if (test1) {
                            System.out.println("Within 1st conditoin");
                            courseList.add(tempCourse);
                        }
                    } else if (!courseNameEmpty && dayEmpty) {
                        if (test2) {
                            System.out.println("Within 1st conditoin");
                            courseList.add(tempCourse);
                        }
                    } else if (!courseNameEmpty && !dayEmpty) {
                        if (test1 && test2) {
                            System.out.println("Within 1st conditoin");
                            courseList.add(tempCourse);
                        }
                    }

                }
                CourseList courseAdapter = new CourseList(activity_gym_member.this, courseList);
                listViewCourses.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }



    /*private void clearErrors() {
        editCourseName.setError(null);
        editWeekDay.setError(null);
    }*/
}