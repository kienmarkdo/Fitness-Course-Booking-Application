package com.example.fitnesscoursebookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class activity_gym_member extends AppCompatActivity {

    // ==================== start of attributes ====================

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

    // ==================== end of attributes ====================

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
                System.out.println("EXITED UN-ENROLL BUTTON");
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

    // =====================  validating methods  =====================

    /**
     * Checks whether the inputted weekday conflicts with another weekday within the gym member's
     *  enrolled list.
     *  NOTE: Assume the parameter String day is in the correct format.
     * @param day Valid weekday as a String
     * @return True if weekday does not conflict with any other classes; False otherwise.
     */
    public boolean validateEnrollDay(String day) {
        for (Course course : enrolledList) {
            if (course.getTime().equals(day)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the day inputted by the user (MON, TUE, WED, THU, FRI)
     * @param day Day as a String
     * @return True if the inputted String is a valid weekday; False otherwise.
     */
    public static boolean validateDayInput(String day) {
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

    /**
     * Validates whether the user has NOT YET enrolled in this course.
     *  Used in the enroll method. If the user is already enrolled in Judo on THU, they cannot
     *  enroll into this course again because they are already in it.
     * @return True if gym member has NOT enrolled in this course yet.
     */
    public boolean validateNotYetEnrolled(String courseName, String courseDay) {

        for (Course course : enrolledList) {
            if (course.getName().equals(courseName) && course.getTime().equals(courseDay)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates whether the number of students has reached the course's preset capacity.
     * Throws an error if the course cannot be found.
     * @return True if there is still room leftover for the gym member to enroll in the course, False otherwise.
     */
    public boolean validateEnrollWithinCapacity (Course course) {

        return (course.getStudentAmount() < course.getCapacity());

    }

    public void updateUsersStudentAmount(int newSA, String courseName, String day) {

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot currentSnapshot: snapshot.getChildren()) {
                    User tempUser = snapshot.getValue(User.class);

                    if (tempUser.getUserType().equals("gymMember")) {
                        GymMember tempMember = snapshot.getValue(GymMember.class);

                        ArrayList<Course> coursesList = tempMember.getCoursesAttending();

                        for (int i = 0; i < courseList.size(); i++) {
                            if (courseList.get(i).getName().equals(courseName) && courseList.get(i).getTime().equals(day)){
                                coursesList.get(i).setStudentAmount(newSA);
                            }
                        }

                        currentSnapshot.child("coursesAttending").getRef().setValue(coursesList);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    // =====================  main methods  =====================

    public void unenroll() {
        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();

        for (int i = 0; i < enrolledList.size(); i++) {
            Course temp = enrolledList.get(i);
            if (courseName.equals(temp.getName()) && day.equals(temp.getTime())) {
                enrolledList.remove(i);
                pushListToDataBase();
                printUnenrollSuccessMessage();
                break;
            }
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Courses");
        Query checkCourse = userRef.orderByChild("name").equalTo(courseName); // gets appropriate name

        checkCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Course tempCourse = postSnapshot.getValue(Course.class);
                        int tempStudentAmount = tempCourse.getStudentAmount();

                        System.out.println("Current coures scanning is " + tempCourse.getName());


                        // since the name is already found through the query, we only need to check
                        //  if the date matches up now
                        if (tempCourse.getTime().equals(day)) {
                            System.out.println("I FOUND THE COURSE (in unenroll() )!!!");

                            postSnapshot.child("studentAmount").getRef().setValue(tempStudentAmount-1);

                            return;
                        }

                    }

                    // no course was found during the specified day
                    editDay.setError("Please enter a class name");
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


        // ===========  cannot find the course  ===========

        editCourseName.setError("Cannot find this course on this day.");
        editCourseName.requestFocus();

    }

    public void placeHolder() {
        System.out.println("aplce");
    }

    /**
     * Updates the gym member's list of courses after the gym member enrolls/un-enrolls from a course.
     */
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

    /**
     * Enrolls in a course
     */
    public void enrollCourse() {

        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();

        // cannot enroll if gym member is already enrolled in this class on this day
        if (!validateNotYetEnrolled(courseName, day)) {
            editCourseName.requestFocus();
            editCourseName.setError("Failed. Already enrolled in this course on this day.");
            return;
        }


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Courses");
        Query checkCourse = userRef.orderByChild("name").equalTo(courseName); // gets appropriate name

        checkCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    // error trapping START
                    if (!validateDayInput(day)) {
                        editDay.setError("Invalid. Enter MON, TUE, WED, THU, or FRI.");
                        editDay.setText("");
                        editDay.requestFocus();
                        return;
                    }

                    if (!validateEnrollDay(day)) {
                        editDay.setError("Failed. Already enrolled in a course on this day.");
                        editDay.requestFocus();
                        return;
                    }


                    // error trapping END

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Course tempCourse = postSnapshot.getValue(Course.class);

                        System.out.println("Current coures scanning is " + tempCourse.getName());


                        // since the name is already found through the query, we only need to check
                        //  if the date matches up now
                        if (tempCourse.getTime().equals(day)) {

                            // error trapping
                            if (!validateEnrollWithinCapacity(tempCourse)) {
                                editCourseName.requestFocus();
                                editCourseName.setError("Failed. Class reached maximum student capacity.");
                                return;
                            }

                            int newSA = tempCourse.getStudentAmount()+1;
                            tempCourse.setStudentAmount(newSA);
                            enrolledList.add(tempCourse);
                            postSnapshot.child("studentAmount").getRef().setValue(tempCourse.getStudentAmount());
                            pushListToDataBase();

                            printEnrollSuccessMessage();
                            updateUsersStudentAmount(newSA, courseName, day);
                            return;
                        }
                    }

                    // no course was found during the specified day
                    editDay.setError("Please enter a class name");
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
     * Displays all courses in which the gym member is enrolled.
     */
    public void listEnrolledCourses() {

        System.out.println(enrolledList);

        if (listEnrolledState == 0) {
            System.out.println("In state zero dispalying enrolled");
            CourseList courseAdapter = new CourseList(activity_gym_member.this, enrolledList);
            listViewCourses.setAdapter(courseAdapter);
            listEnrolledState = 1;
        }

        else {
            databaseCourses.addListenerForSingleValueEvent(new ValueEventListener() {
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

    /**
     * Searches for a course
     */
    public void searchCourse() {
        //clearErrors();

        String courseName = editCourseName.getText().toString();
        String day = editDay.getText().toString();

        boolean courseNameEmpty = courseName.equals("");
        boolean dayEmpty = day.equals("");

        if (courseNameEmpty && dayEmpty) {
            databaseCourses.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    courseList.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Course tempCourse = postSnapshot.getValue(Course.class);
                        courseList.add(tempCourse);
                    }
                    CourseList courseAdapter = new CourseList(activity_gym_member.this, courseList);
                    listViewCourses.setAdapter(courseAdapter);

                    clearErrors();
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



    /**
     * Displays a small pop-up at the bottom of the screen indicating enroll course was successful
     */
    private void printEnrollSuccessMessage() {

        // hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String courseName = editCourseName.getText().toString();
        String weekDay = editDay.getText().toString();

        clearErrors();

        // displays the success message
        Context context = getApplicationContext();
        CharSequence text = "Successfully enrolled in " + courseName +
                " on " + weekDay  + "!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Displays a small pop-up at the bottom of the screen indicating unenroll course was successful
     */
    private void printUnenrollSuccessMessage() {

        // hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String courseName = editCourseName.getText().toString();
        String weekDay = editDay.getText().toString();

        clearErrors();

        // displays the success message
        Context context = getApplicationContext();
        CharSequence text = "Successfully un-enrolled in " + courseName +
                " on " + weekDay  + "!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    } // end of printUserAddedSuccessMessage()

    /**
     * Helper method that clears all errors in the input text fields
     */
    private void clearErrors() {
        // clear text fields and remove error messages
        editCourseName.getText().clear();
        editDay.getText().clear();
        editCourseName.setError(null);
        editDay.setError(null);
    }
}