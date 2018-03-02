package com.example.gyanprakash.pushnotificationdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    private final ArrayList<ProfileModel> profileModels;
    public CustomList(Activity context,
                      ArrayList<ProfileModel> profileModels) {
        super(context, R.layout.list_item);
        this.context = context;
        this.profileModels = profileModels;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.profile_text);
        txtTitle.setText(profileModels.get(position).getProfileName());
        return rowView;
    }
}