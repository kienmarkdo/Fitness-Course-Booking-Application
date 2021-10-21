package com.example.fitnesscoursebookingapp;

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import android.widget.TextView;
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
    Button createCourseButton, editCourseButton, deleteCourseButton, deleteUserButton;
    int height, height1, height2, height3;

    EditText addCourseInput;
    EditText addCourseDescriptionInput;
    EditText courseNameChangeInput;
    EditText courseDescriptionChangeInput;
    EditText deleteCourseNameInput;
    EditText deleteUsernameInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
        courseNameChangeInput = findViewById(R.id.courseNameChangeInput);
        courseDescriptionChangeInput = findViewById(R.id.courseDescriptionChangeInput);
        deleteCourseNameInput = findViewById(R.id.deleteCourseNameInput);
        deleteUsernameInput = findViewById(R.id.deleteUserNameInput);

        // TODO: setting on click listener on createCourseButton will cause the app to crash after logging in
        //  Commenting out this block of code below will stop the app from crashing.
        createCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCourse();
            }
        });

        /*
        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCourse();
            }

        });

        deleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
            }

        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }

        });*/


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
    }

    //************** CODE TO CREATE THE DROPDOWN MENU FOR ACTIVITY_ADMIN.XML****************
    private void expand(RelativeLayout layout, int layoutHeight) {
        layout.setVisibility(View.VISIBLE);
        ValueAnimator animator = slideAnimator(layout, 0, layoutHeight);
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
                    Course newCourse = new Course(courseNameStr, courseDescriptionStr);
                    DatabaseReference ref = reference.push(); // add new course here
                    ref.child("experienceLevel").setValue("");
                    ref.child("hourDuration").setValue(0);
                    ref.child("teacher").setValue("");
                    ref.child("time").setValue("");
                    printCourseAddedSuccessMessage();
                } // end of outer if/else

            } // end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // end of onCalled()
        }); // end of checkCourse listener

    } // end of createCourse()

    /*
    private void editCourse(){

    }

    private void deleteCourse(){

    }

    private void deleteUser(){

    }*/


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
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    } // end of printCourseAddedSuccessMessage()


} // end of activity_admin

