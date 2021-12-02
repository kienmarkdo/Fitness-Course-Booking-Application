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

    DatabaseReference databaseCourses;

    static String[] dayStrings = {"MON", "TUE", "WED", "THU", "FRI"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        courseName = (TextView) findViewById(R.id.courseNameView);
        weekDay = (TextView) findViewById(R.id.weekDayView);

        editCourseName = (EditText) findViewById(R.id.editCourseName);
        editDay = (EditText) findViewById(R.id.editWeekDay);

        enrollButton = (Button) findViewById(R.id.enrollButton);
        unenrollButton = (Button) findViewById(R.id.unenrollButton);
        listEnrolledButton= (Button) findViewById(R.id.listEnrolledButton);

        courseList = new ArrayList<>();
        databaseCourses = FirebaseDatabase.getInstance().getReference("Courses");

        listViewCourses= findViewById(R.id.courseList);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrollCourse();
            }
        });

        unenrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCourse();
            }
        });

        listEnrolledButton.setOnClickListener(new View.OnClickListener() {
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
                CourseList courseAdapter = new CourseList(activity_gym_member.this, courseList);
                listViewCourses.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void enrollCourse() {
        clearErrors();

        String courseName = editCourseName.getText().toString();
        String experience = editDay.getText().toString();


        boolean problem = false;
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
        clearErrors();

        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();

        boolean problem = false;
        if (!verifyValidName(courseName) ) {
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
        clearErrors();

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

        boolean problem = false;
        if (!verifyValidName(courseName)) {
            editCourseName.setError("Must set a name");
            editCourseName.requestFocus();
            problem = true;
        } if (!verifyValidDay(day)) {
            editDay.setError("Must be a valid day");
            editDay.requestFocus();
            problem = true;
        } if (!capacityLimit.isEmpty() && !verifyValidCapacityLimit(capacityLimit)) {
            editCapacityLimit.setError("Must be a number larger than 0");
            editCapacityLimit.requestFocus();
            problem = true;
        } if (!startTime.isEmpty() && !verifyValidStartTime(startTime)) {
            editStartTime.setError("Must be between 0 and 23 inclusive");
            editStartTime.requestFocus();
            problem = true;
        } if (!duration.isEmpty() && !verifyDuration(duration)) {
            editDuration.setError("Must be 1 or 1.5");
            editDuration.requestFocus();
            problem = true;
        }
        if (problem) {
            return;
        }


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

                                // editing the actual values in the database
                                if (!experience.isEmpty()) {
                                    course.child("experienceLevel").setValue(experience);
                                } if (!duration.isEmpty()) {
                                    course.child("hourDuration").setValue(Float.parseFloat(duration));
                                } if (!capacityLimit.isEmpty()) {
                                    course.child("capacity").setValue(Integer.parseInt(capacityLimit));
                                } if (!newDay.isEmpty()) {
                                    course.child("time").setValue(newDay);
                                } if (!startTime.isEmpty()) {
                                    course.child("startTime").setValue(Float.parseFloat(startTime));
                                }
                            } else {
                                editCourseName.setError("Must edit your own course");
                            }

                            return;
                        }
                    }

                    // if there is no class on the specified day
                    editDay.setError("There is no class to edit on this day");
                    editDay.requestFocus();

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
        clearErrors();

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

    private void clearErrors() {
        editCourseName.setError(null);
        editWeekDay.setError(null);
    }
}