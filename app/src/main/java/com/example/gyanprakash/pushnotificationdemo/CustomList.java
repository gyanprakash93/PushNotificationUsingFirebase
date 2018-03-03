package com.example.gyanprakash.pushnotificationdemo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends RecyclerView.Adapter<CustomList.ViewHolder> {

    private ArrayList<ProfileModel> profileModels;

    private Context context;

    public CustomList(Context context, ArrayList<ProfileModel> profileModels) {
        this.context = context;
        this.profileModels=profileModels;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("getProfileName",profileModels.get(position).getProfileName());
holder.profileName.setText(profileModels.get(position).getProfileName());

    }

    @Override
    public int getItemCount() {
        return profileModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView profileName;

        public ViewHolder(View itemView) {
            super(itemView);

            profileName = itemView.findViewById(R.id.profile_text);
        }
    }
}
