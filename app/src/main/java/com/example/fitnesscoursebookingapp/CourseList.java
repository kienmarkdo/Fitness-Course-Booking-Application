
package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

/**
 * Only exists to display a courselist into a UI.
 */
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
        TextView textViewInstructor = (TextView) listViewItem.findViewById(R.id.instructorNameView);
        TextView textViewStartTime = (TextView) listViewItem.findViewById(R.id.startTimeView);

        Course course = courses.get(position);
        String nameStr = "Name: " + course.getName();
        System.out.println(nameStr);
        textViewName.setText(nameStr);

        String experienceStr = "Expr: " + course.getExperienceLevel();
        textViewExperience.setText(experienceStr);

        String courseStr = "Date: " + course.getTime();
        textViewDay.setText(courseStr);

        String capStr = "Cap: " + String.valueOf(course.getCapacity());
        textViewCapacity.setText(capStr);

        String startStr = "Start Time: " + String.valueOf(course.getStartTime());
        //System.out.println("start str: " + startStr);
        textViewStartTime.setText(startStr);

        String durStr = "Dur: " + String.valueOf(course.getHourDuration());
        textViewDuration.setText(durStr);


        String insStr = "Inst: " + course.getTeacher().getLegalName();
        textViewInstructor.setText(insStr);



        return listViewItem;
    }
}
