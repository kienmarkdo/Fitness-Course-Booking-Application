
package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import java.util.List;

public class CourseList extends ArrayAdapter<Course> {

    private Activity context;
    List<Course> courses;

    public CourseList(@NonNull Activity context, @NonNull List<Course> courses) {
        super(context, R.layout.layout_courses_list, courses );
        this.context = context;
        this.courses = courses;

    }

    public View getView(int position, View ConvertView, ViewGroup Parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_courses_list,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);

        Course course = courses.get(position);
        textViewName.setText(course.getName());
        return listViewItem;


    }
}
