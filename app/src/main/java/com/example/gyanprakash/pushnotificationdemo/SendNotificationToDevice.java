package com.example.gyanprakash.pushnotificationdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Xeper on 3/2/2018.
 */

public class SendNotificationToDevice extends AppCompatActivity {
    private static final String UPLOAD_URL = "http://192.168.0.9/push_notification_demo/fetchin_profile.php";
    RecyclerView recyclerView;
    ArrayList<ProfileModel> profileModels;
    ArrayAdapter modelArrayAdapter;
    CustomList customList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification_to_device);

        recyclerView = findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileModels = new ArrayList<>();
        customList = new CustomList(this, profileModels);
        recyclerView.setAdapter(customList);

        new FetchProfileAsync().execute();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SendNotificationToDevice.this);

                View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null, true);
                builder.setView(customView);

                final EditText title = customView.findViewById(R.id.title);
                final EditText message = customView.findViewById(R.id.message);
                Button send = customView.findViewById(R.id.send);
                Button cancel = customView.findViewById(R.id.cancel);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {

                            new SendNotificationAsync(profileModels.get(position).getFcmId(), title.getText().toString(), message.getText().toString()).execute();

                        } else if (title.getText().toString().isEmpty() && message.getText().toString().isEmpty()) {
                            title.setError("Empty field");
                            message.setError("Empty field");
                        } else if (title.getText().toString().isEmpty()) {
                            title.setError("Empty field");
                        } else {
                            message.setError("Empty field");
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }


    class FetchProfileAsync extends AsyncTask<Void, Void, Boolean> {
        String result;
        String jsonStr;
        String status, count = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            String data = null;
            String s1 = null, s2 = null;
            String data1 = null;
            try {
                data = URLEncoder.encode("method", "UTF-8")
                        + "=" + URLEncoder.encode("fetch_profile", "UTF-8");
                Log.e("jsonarray", data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Http sh = new Http();
            jsonStr = sh.makeServiceCall(UPLOAD_URL, data);
            Log.e("Response", jsonStr);
            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String profileId = jsonObject.getString("profile_id");
                        Log.e("id", profileId);
                        String profileName = jsonObject.getString("profile_name");
                        Log.e("name", profileName);
                        String fcmId = jsonObject.getString("fcm_id");
                        Log.e("fcmid", fcmId);
                        ProfileModel profileModel = new ProfileModel();
                        profileModel.setProfileId(profileId);
                        profileModel.setProfileName(profileName);
                        profileModel.setFcmId(fcmId);

                        profileModels.add(profileModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Boolean result) {
            //modelArrayAdapter=new ArrayAdapter<ProfileModel>(SendNotificationToDevice.this,android.R.layout.activity_list_item,profileModels);
            customList = new CustomList(SendNotificationToDevice.this, profileModels);
            customList.notifyDataSetChanged();
        }
    }


    class SendNotificationAsync extends AsyncTask<Void, Void, Boolean> {
        String jsonStr;
        String status;
        String fcm_id, title, message;

        public SendNotificationAsync(String fcm_id, String title, String message) {
            this.fcm_id = fcm_id;
            this.title = title;
            this.message = message;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String data = null;
            String s1 = null, s2 = null;
            String data1 = null;
            try {
                data = URLEncoder.encode("method", "UTF-8")
                        + "=" + URLEncoder.encode("send_notification", "UTF-8");
                data += "&" + URLEncoder.encode("fcm_token", "UTF-8")
                        + "=" + URLEncoder.encode(fcm_id, "UTF-8");
                data += "&" + URLEncoder.encode("title", "UTF-8")
                        + "=" + URLEncoder.encode(title, "UTF-8");
                data += "&" + URLEncoder.encode("message", "UTF-8")
                        + "=" + URLEncoder.encode(message, "UTF-8");
                data += "&" + URLEncoder.encode("img_url", "UTF-8")
                        + "=" + URLEncoder.encode("health_package.jpg", "UTF-8");
                Log.e("jsonarray", data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Http sh = new Http();
            jsonStr = sh.makeServiceCall("http://192.168.0.9/push_notification_demo/send_notification_to_user.php", data);
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Boolean result) {

        }
    }

}
