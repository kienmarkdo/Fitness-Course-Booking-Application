
package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class CourseList extends ArrayAdapter<Course> {

    private Activity context;
    List<Course> courses;
    //HashMap<String, course> map;


    public CourseList(Activity context, List<Course> courses) {

        super(context, R.layout.layout_courses_list, courses);
        this.context = context;
        this.courses = courses;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_courses_list, null, true);


        TextView textViewName = (TextView) listViewItem.findViewById(R.id.nameTextView);
        TextView textViewExperience = (TextView) listViewItem.findViewById(R.id.experienceTextView);
        TextView textViewDay = (TextView) listViewItem.findViewById(R.id.dayTextView);
        TextView textViewCapacity = (TextView) listViewItem.findViewById(R.id.capacityLimitView);
        TextView textViewDuration = (TextView) listViewItem.findViewById(R.id.durationTextView);

        Course course = courses.get(position);
        textViewName.setText(course.getName());
        textViewExperience.setText(course.getExperienceLevel());
        textViewDay.setText(course.getExperienceLevel());
        textViewCapacity.setText(course.getExperienceLevel());
        textViewDuration.setText(course.getExperienceLevel());

        return listViewItem;
    }
}
