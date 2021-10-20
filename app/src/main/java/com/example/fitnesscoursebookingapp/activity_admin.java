package com.example.fitnesscoursebookingapp;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    EditText deleteUserNameInput;


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
        deleteUserNameInput = findViewById(R.id.deleteUserNameInput);

        createCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCourse();
            }

        });

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
                    expand(relativeLayout, height);
                } else {
                    collapse(relativeLayout);
                }
                break;

            case R.id.viewmore1:
                if (relativeLayout1.getVisibility() == View.GONE) {
                    expand(relativeLayout1, height1);
                } else {
                    collapse(relativeLayout1);
                }
                break;

            case R.id.viewmore2:
                if (relativeLayout2.getVisibility() == View.GONE) {
                    expand(relativeLayout2, height2);
                } else {
                    collapse(relativeLayout2);
                }
                break;

            case R.id.viewmore3:
                if (relativeLayout3.getVisibility() == View.GONE) {
                    expand(relativeLayout3, height3);
                } else {
                    collapse(relativeLayout3);
                }
                break;
        }
    }

    // **** METHODS FOR ADMIN BUTTONS ****

    private void createCourse(){

    }

    private void editCourse(){

    }

    private void deleteCourse(){

    }

    private void deleteUser(){

    }

} // end of activity_admin

