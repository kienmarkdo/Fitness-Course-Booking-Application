package com.example.fitnesscoursebookingapp;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_admin extends Activity implements View.OnClickListener {

    RelativeLayout relativeLayout, relativeLayout1, relativeLayout2, relativeLayout3;
    Button viewmore, viewmore1, viewmore2, viewmore3;
    Button createCourseBtn, editCourseBtn, deleteCourseBtn, deleteUserBtn;
    int height, height1, height2, height3;

    EditText addCourseInput;
    EditText addCourseDescriptionInput;
    EditText courseNameChangeInput;
    EditText courseDescriptionChangeInput;
    EditText deleteCourseNameInput;
    EditText deleteUsernameInput;
    EditText editCourseNameInput;
    EditText editCourseDescriptionInput;
    EditText newCourseNameInput;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: Refactor program to listView after Deliverable 1
        //  currently takes in text as input from the user
        //  the reason we want listView is because:
        //  1. It shows the user what options they have
        //  2. It removes error from the user keyboard input (i.e.: a course that doesn't exist OR an empty string)

        // initializers for the Dropdown menu in Admin
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        relativeLayout = (RelativeLayout) findViewById(R.id.expandable);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.expandable1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.expandable2);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.expandable3);

        viewmore = (Button) findViewById(R.id.viewmore);
        viewmore1 = (Button) findViewById(R.id.viewmore1);
        viewmore2 = (Button) findViewById(R.id.viewmore2);
        viewmore3 = (Button) findViewById(R.id.viewmore3);

        viewmore.setOnClickListener(this);
        viewmore1.setOnClickListener(this);
        viewmore2.setOnClickListener(this);
        viewmore3.setOnClickListener(this);

        // initializers for different Admin Commands
        addCourseInput = findViewById(R.id.addCourseInput);
        addCourseDescriptionInput = findViewById(R.id.addCourseDescriptionInput);
        courseDescriptionChangeInput = findViewById(R.id.courseDescriptionChangeInput);
        deleteCourseNameInput = findViewById(R.id.deleteCourseNameInput);
        deleteUsernameInput = findViewById(R.id.deleteUserNameInput);
        editCourseNameInput = findViewById(R.id.courseNameChangeInput);
        editCourseDescriptionInput = findViewById(R.id.courseDescriptionChangeInput);
        newCourseNameInput = findViewById(R.id.newNameChangeInput);

        createCourseBtn = (Button) findViewById(R.id.createCourseButton);
        editCourseBtn = (Button) findViewById(R.id.editCourseButton);
        deleteCourseBtn = (Button) findViewById(R.id.deleteCourseButton);
        deleteUserBtn = (Button) findViewById(R.id.deleteUserButton);

        createCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCourse();
            }
        });

//        editCourseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editCourse();
//            }
//
//        });

        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
            }

        });

        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });

        editCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCourse();
            }
        });


        relativeLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        relativeLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout1.setVisibility(View.GONE);
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout3.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        relativeLayout.measure(widthSpec, heightSpec);
                        height = relativeLayout.getMeasuredHeight();
                        relativeLayout1.measure(widthSpec, heightSpec);
                        height1 = relativeLayout.getMeasuredHeight();
                        relativeLayout2.measure(widthSpec, heightSpec);
                        height2 = relativeLayout.getMeasuredHeight();
                        relativeLayout3.measure(widthSpec, heightSpec);
                        height3 = relativeLayout.getMeasuredHeight();
                        return true;
                    }
                });
    } // end of relativeLayout

    //************** CODE TO CREATE THE DROPDOWN MENU FOR ACTIVITY_ADMIN.XML****************
    private void expand(RelativeLayout layout, int layoutHeight) {
        layout.setVisibility(View.VISIBLE);
        ValueAnimator animator = slideAnimator(layout, 0, 1500); // TODO: Third parameter was layoutHeight before. Fix this so that the height is automatic.
        animator.start();
    }

    private void collapse(final RelativeLayout layout) {
        int finalHeight = layout.getHeight();
        ValueAnimator mAnimator = slideAnimator(layout, finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(final RelativeLayout layout, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                layoutParams.height = value;
                layout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewmore:
                if (relativeLayout.getVisibility() == View.GONE) {
                    collapse(relativeLayout1);
                    collapse(relativeLayout2);
                    collapse(relativeLayout3);
                    expand(relativeLayout, height);
                } else {
                    collapse(relativeLayout);
                }
                break;

            case R.id.viewmore1:
                if (relativeLayout1.getVisibility() == View.GONE) {
                    collapse(relativeLayout);
                    collapse(relativeLayout2);
                    collapse(relativeLayout3);
                    expand(relativeLayout1, height1);
                } else {
                    collapse(relativeLayout1);
                }
                break;

            case R.id.viewmore2:
                if (relativeLayout2.getVisibility() == View.GONE) {
                    collapse(relativeLayout);
                    collapse(relativeLayout1);
                    collapse(relativeLayout3);
                    expand(relativeLayout2, height2);
                } else {
                    collapse(relativeLayout2);
                }
                break;

            case R.id.viewmore3:
                if (relativeLayout3.getVisibility() == View.GONE) {
                    collapse(relativeLayout);
                    collapse(relativeLayout1);
                    collapse(relativeLayout2);
                    expand(relativeLayout3, height3);
                } else {
                    collapse(relativeLayout3);
                }
                break;
        }
    }

    // **** METHODS FOR ADMIN BUTTONS ****

    /**
     * Creates a new course by taking in the name and description of the new course as user input
     * Adds the course info into the firebase database.
     */
    private void createCourse() {

        String courseNameStr = addCourseInput.getText().toString();
        String courseDescriptionStr = addCourseDescriptionInput.getText().toString();

        // =======  check if the course name is empty or not  =======
        if (courseNameStr.equals("")) {
            addCourseInput.setError("The course name cannot be blank.");
            addCourseInput.requestFocus();
            return;
        }

        // NOTE: The input is case sensitive, which means the course "Tennis" and "tennis" may co-exist at the same time

        // =======  check if the course exists in the database or not  =======

        // fetches instance of database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        // Orders in search for the course name
        Query checkCourse = reference.orderByChild("name").equalTo(courseNameStr);

        checkCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // checks whether the username already exists or not
                if (dataSnapshot.exists()) {
                    addCourseInput.setError("This username already exists. Please re-enter a new course name.");
                    addCourseInput.requestFocus();
                } else {
                    addCourseInput.setError(null);
                    DatabaseReference ref = reference.push(); // add new course here
                    ref.setValue(new Course(courseNameStr, courseDescriptionStr, "Unassigned", 0, new Instructor("Unassigned"), "Unassigned", 0 ));
                    printCourseAddedSuccessMessage();
                } // end of outer if/else

            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of checkCourse listener

    } // end of createCourse()


    private void editCourse(){
        String courseNameStr = editCourseNameInput.getText().toString();
        String courseDescriptionStr = editCourseDescriptionInput.getText().toString();
        String courseNewNameStr = newCourseNameInput.getText().toString();

        // =======  check if the course name is empty or not  =======
        if (courseNameStr.equals("")) {
            editCourseNameInput.setError("The course name cannot be blank.");
            editCourseNameInput.requestFocus();
            return;
        }


        // NOTE: The input is case sensitive, which means the course "Tennis" and "tennis" may co-exist at the same time

        // =======  check if the course exists in the database or not  =======

        // fetches instance of database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        // Orders in search for the course name
        Query checkCourse = reference.orderByChild("name").equalTo(courseNameStr);

        checkCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // checks whether the username already exists or not
                if (!dataSnapshot.exists()) {
                    editCourseNameInput.setError("Could not find the course with given name.");
                    editCourseNameInput.requestFocus();
                } else {
                    editCourseNameInput.setError(null);
                    DatabaseReference course = dataSnapshot.getChildren().iterator().next().getRef();
                    course.child("description").setValue(courseDescriptionStr);

                    if (!courseNewNameStr.equals("")) {
                        newCourseNameInput.setError(null);
                        course.child("name").setValue(courseNewNameStr);
                    }
                    printCourseEditSuccessMessage();
                } // end of outer if/else

            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of checkCourse listener
    }

    private void deleteCourse(){

        // NOTE: The reason we are NOT checking to see if the username to be deleted or not
        //  is because we are selecting the pre-existing user from a list view. Therefore, we cannot
        //  have an inputted username that does not exist.

        String deleteCourseStr = deleteCourseNameInput.getText().toString();

        // fetches instance of database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        // Orders in search for the course name
        Query checkUser = reference.orderByChild("name").equalTo(deleteCourseStr);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    deleteCourseNameInput.setError("This course name does not exist.");
                    deleteCourseNameInput.requestFocus();
                } else {
                    deleteCourseNameInput.setError(null);
                    // remove the username here
                    dataSnapshot.getChildren().iterator().next().getRef().removeValue();
                    printCourseRemovedSuccessMessage();
                }


            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of listener
    }

    /**
     * Deleting the user from the firebase
     * Instead of directly deleting from firebase, performs the method through the hashmap in Gym
     */
    private void deleteUser() {


        // NOTE: The reason we are NOT checking to see if the username to be deleted or not
        //  is because we are selecting the pre-existing user from a list view. Therefore, we cannot
        //  have an inputted username that does not exist.

        String deleteUsernameStr = deleteUsernameInput.getText().toString();

        // fetches instance of database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        // Orders in search for the course name
        Query checkUser = reference.orderByChild("username").equalTo(deleteUsernameStr);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    deleteUsernameInput.setError("This username does not exist.");
                    deleteUsernameInput.requestFocus();
                } else {
                    deleteUsernameInput.setError(null);
                    // remove the username here
                    dataSnapshot.getChildren().iterator().next().getRef().removeValue();
                    printUserRemovedSuccessMessage();
                }


            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of listener

    } // end of deleteUser()

    // ================  Print Success Messages  ================

    /**
     * Displays a small pop-up at the bottom of the screen indicating that adding a course was a success
     */
    private void printCourseAddedSuccessMessage() {

        // hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        addCourseInput.getText().clear();
        addCourseDescriptionInput.getText().clear();

        // displays the success message
        Context context = getApplicationContext();
        CharSequence text = "Course Added Successfully!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    } // end of printCourseAddedSuccessMessage()

    /**
     * Displays a small pop-up at the bottom of the screen indicating that removing user was a success
     */
    private void printUserRemovedSuccessMessage() {

        // hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        deleteUsernameInput.getText().clear();

        // displays the success message
        Context context = getApplicationContext();
        CharSequence text = "User Removed Successfully!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    } // end of printUserRemovedSuccessMessage()

    private void printCourseRemovedSuccessMessage() {
        // hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        deleteCourseNameInput.getText().clear();

        // displays the success message
        Context context = getApplicationContext();
        CharSequence text = "Course Removed Successfully!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void printCourseEditSuccessMessage() {
        // hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        editCourseNameInput.getText().clear();
        editCourseDescriptionInput.getText().clear();
        newCourseNameInput.getText().clear();

        // displays the success message
        Context context = getApplicationContext();
        CharSequence text = "Course description edited successfully!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
} // end of activity_admin

