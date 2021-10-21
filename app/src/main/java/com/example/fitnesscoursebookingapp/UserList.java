package com.example.fitnesscoursebookingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<User> {

    private Activity context;

    List<User> users;

    //Need toadd prodcut frontend
    public UserList(Activity context, List<User> users) {
        super(context, R.layout.layout_user_list , users);
        this.context = context;
        this.users = users;
    }


    public View getView(int index, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View singleListView = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView nameText = singleListView.findViewById(R.id.nameText);
        TextView roleText = singleListView.findViewById(R.id.roleText);

        User localUser = users.get(index);

        nameText.setText(localUser.getUsername());
        nameText.setText(localUser.getUserType());

        return singleListView;


    }
}
