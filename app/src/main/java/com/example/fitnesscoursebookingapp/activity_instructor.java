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
    TextView instructor;
    TextView startTime;

    EditText editCourseName;
    EditText editExperience;
    EditText editDay;
    EditText editCapacityLimit;
    EditText editDuration;
    EditText editNewDay;
    EditText editInstructor;
    EditText editStartTime;

    Button createCourseButton;
    Button editCourseButton;
    Button cancelCourseButton;
    Button searchButton;

    ListView listViewCourses;

    List<Course> courseList;

    DatabaseReference databaseCourses;

    static String[] dayStrings = {"MON", "TUE", "WED", "THU", "FRI"};

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
        TextView textViewInstructor = (TextView) findViewById(R.id.instructorView);
        TextView textViewStartTime = (TextView) findViewById(R.id.startTime);

        editCourseName = (EditText) findViewById(R.id.editCourseName);
        editExperience = (EditText) findViewById(R.id.editExperience);
        editDay= (EditText) findViewById(R.id.editDay);
        editCapacityLimit = (EditText) findViewById(R.id.editCapacityLimit);
        editDuration= (EditText) findViewById(R.id.editDuration);
        editNewDay = (EditText) findViewById(R.id.newDateView);
        editInstructor = (EditText) findViewById(R.id.editInstructor);
        editStartTime = (EditText) findViewById(R.id.editStartTime);

        createCourseButton = (Button) findViewById(R.id.createCourse);
        cancelCourseButton = (Button) findViewById(R.id.cancelButton);
        editCourseButton = (Button) findViewById(R.id.editCourse);
        searchButton = (Button) findViewById(R.id.searchButton);

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
                CourseList courseAdapter = new CourseList(activity_instructor.this, courseList);
                listViewCourses.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    public static boolean verifyValidCapacityLimit(String capacityLimit) {
        try {

            // Parsing user input to integer
            // using the parseInt() method
            int capLim = Integer.parseInt(capacityLimit);

            if (capLim <= 0) {
                return false;
            }

        }

        // Catch block to handle NumberFormatException
            catch (NumberFormatException e) {

            // Print the message if exception occured
            System.out.println("NumberFormatException occured");
            return false;
        }


        return true;
    }


    public static boolean verifyValidStartTime(String startTime) {

        try {

            // Parsing user input to integer
            // using the parseInt() method
            int temp = Integer.parseInt(startTime);

            if (temp < 0 || temp >= 24) {
                return false;
            }

        }

        // Catch block to handle NumberFormatException
        catch (NumberFormatException e) {

            // Print the message if exception occured
            System.out.println("NumberFormatException occured");
            return false;
        }


        return true;
    }


    public static boolean verifyValidDay(String day) {
        if (day == null || day.isEmpty()) {
            return false;
        }

        for (int i = 0; i < 5; i++) {
            if (day.equals(dayStrings[i])) {
                return true;
            }
        }
        return false;
    }


    public static boolean verifyDuration(String _duration) {
        try {
            float duration = Float.parseFloat(_duration);

            if (duration != 1.0f && duration != 1.5f) {
                return false;
            }
        } catch (NumberFormatException e) {
            // Print the message if exception occured
            System.out.println("NumberFormatException occured");
            return false;
        }

        return true;
    }


    public void createCourse() {

        // TODO: Error trap to see if the user inputted a valid DAY OF THE WEEK or not
        //  this way, we can error trap from a list of 7 abbreviated strings (MON, TUE, WED...)


        String courseName = editCourseName.getText().toString();
        String experience = editExperience.getText().toString();
        String day = editDay.getText().toString();
        String capacityLimit = editCapacityLimit.getText().toString();
        String duration = editDuration.getText().toString();
        String startTime = editStartTime.getText().toString();

        boolean problem = false;
        if (!verifyDuration(duration)) {
            editDuration.setError("Must be 1 or 1.5");
            editDuration.requestFocus();
            problem = true;
        } if (!verifyValidDay(day)) {
            editDay.setError("Must be a valid day");
            editDay.requestFocus();
            problem = true;
        } if (!verifyValidStartTime(startTime)) {
            editStartTime.setError("Must be between 0 and 23 inclusive");
            editStartTime.requestFocus();
            problem = true;
        } if (!verifyValidCapacityLimit(capacityLimit)) {
            editCapacityLimit.setError("Must be a number larger than 0");
            editCapacityLimit.requestFocus();
            problem = true;
        } if (experience == null || experience.isEmpty()) {
            editExperience.setError("Must set an experience level");
            editExperience.requestFocus();
            problem = true;
        } if (courseName == null || courseName.isEmpty()) {
            editCourseName.setError("Must set a name");
            editCourseName.requestFocus();
            problem = true;
        }
        if (problem) {
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        // Orders in search for the course name
        Query checkCourse = reference.orderByChild("name").equalTo(courseName);

        checkCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String description = null;

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Course tempCourse = postSnapshot.getValue(Course.class);

                        if (tempCourse.getTime().equals(day)) {
                            editDay.setError(courseName + " on " + day + " already scheduled by " + tempCourse.getTeacher().getLegalName());
                            return;
                        } else if (tempCourse.getCapacity() == 0) {
                            description = tempCourse.getDescription();
                        }
                    }

                    editCourseName.setError(null);
                    editCourseName.setError(null);
                    DatabaseReference ref = reference.push(); // add new course here
                    ref.setValue(new Course(courseName, description, day, Float.parseFloat(duration), new Instructor(instructorId), experience, Integer.parseInt(capacityLimit), Float.parseFloat(startTime)));
                    editCourseName.setText("");
                    editExperience.setText("");
                    editDay.setText("");
                    editCapacityLimit.setText("");
                    editDuration.setText("");
                    editStartTime.setText("");

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

        boolean problem = false;
        if (courseName == null || courseName.isEmpty()) {
            editCourseName.setError("Must set a name");
            editCourseName.requestFocus();
            problem = true;
        } if (!verifyValidDay(day)) {
            editDay.setError("Must be a valid day");
            editDay.requestFocus();
            problem = true;
        }
        if (problem) {
            return;
        }

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

                            if (tempCourse.getTeacher().getLegalName().equals(instructorId)) {
                                editCourseName.setError(null);
                                editCourseName.setError(null);
                                postSnapshot.getRef().removeValue();
                                editCourseName.setText("");
                                editDay.setText("");
                                return;
                            }

                            else {
                                editCourseName.setError("Must cancel your own course.");
                                editCourseName.requestFocus();
                                return;
                            }
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

    /**
     * Edit a course that this instructor currently teaches (a course that is within this java class's courseList).
     */
    public void editCourse() {

        // TODO: Error trap to see if the user inputted a valid DAY OF THE WEEK or not
        //  this way, we can error trap from a list of 7 abbreviated strings (MON, TUES, WED...)


        String courseName = editCourseName.getText().toString();
        String experience = editExperience.getText().toString();
        String day = editDay.getText().toString();
        String capacityLimit = editCapacityLimit.getText().toString();
        String duration = editDuration.getText().toString();
        String newDay = editNewDay.getText().toString();
        String startTime = editStartTime.getText().toString();

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

                            if (tempCourse.getTeacher().getLegalName().equals(instructorId)) {
                                editCourseName.setError(null);
                                DatabaseReference course = dataSnapshot.getChildren().iterator().next().getRef();
                                course.child("experienceLevel").setValue(experience);
                                course.child("hourDuration").setValue(Float.parseFloat(duration));
                                course.child("capacity").setValue(Integer.parseInt(capacityLimit));
                                course.child("time").setValue(newDay);
                                course.child("startTime").setValue(Float.parseFloat(startTime));
                                return;
                            }
                        }
                    }



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

        String courseName = editCourseName.getText().toString();
        String instructor = editInstructor.getText().toString();

        boolean courseNameEmpty = courseName.equals("");
        boolean instructorEmpty = instructor.equals("");

        if (courseNameEmpty && instructorEmpty) {
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

            return;
        }



        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    Course tempCourse = postSnapshot.getValue(Course.class);

                    boolean test1 = tempCourse.getTeacher().getLegalName().equals(instructor);
                    boolean test2 = tempCourse.getName().equals(courseName);

                    System.out.println("test1: " + test1);
                    System.out.println("test2: " + test2);

                    if (courseNameEmpty && !instructorEmpty) {
                        if (test1) {
                            System.out.println("Within 1st conditoin");
                            courseList.add(tempCourse);
                        }
                    }

                    else if (!courseNameEmpty && instructorEmpty) {
                        if (test2) {
                            System.out.println("Within 1st conditoin");
                            courseList.add(tempCourse);
                        }
                    }

                    else if (!courseNameEmpty && !instructorEmpty) {
                        if (test1 && test2) {
                            System.out.println("Within 1st conditoin");
                            courseList.add(tempCourse);
                        }
                    }

                }
                CourseList courseAdapter = new CourseList(activity_instructor.this, courseList);
                listViewCourses.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
}