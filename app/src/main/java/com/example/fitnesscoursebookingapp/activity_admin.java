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

    RelativeLayout relativeLayout, relativeLayout1, relativeLayout2;
    Button viewmore, viewmore1, viewmore2;
    int height, height1, height2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        relativeLayout = (RelativeLayout) findViewById(R.id.expandable);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.expandable1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.expandable2);

        viewmore = (Button) findViewById(R.id.viewmore);
        viewmore1 = (Button) findViewById(R.id.viewmore1);
        viewmore2 = (Button) findViewById(R.id.viewmore2);

        viewmore.setOnClickListener(this);
        viewmore1.setOnClickListener(this);
        viewmore2.setOnClickListener(this);


        relativeLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        relativeLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout1.setVisibility(View.GONE);
                        relativeLayout2.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        relativeLayout.measure(widthSpec, heightSpec);
                        height = relativeLayout.getMeasuredHeight();
                        relativeLayout1.measure(widthSpec, heightSpec);
                        height1 = relativeLayout.getMeasuredHeight();
                        relativeLayout2.measure(widthSpec, heightSpec);
                        height2 = relativeLayout.getMeasuredHeight();
                        return true;
                    }
                });
    }


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
        }
    }


} // end of activity_admin