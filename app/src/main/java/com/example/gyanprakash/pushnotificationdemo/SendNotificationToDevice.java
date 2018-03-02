package com.example.gyanprakash.pushnotificationdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

class SendNotificationToDevice extends AppCompatActivity {
    private static final String UPLOAD_URL ="http://192.168.0.24/push_notification_demo/fetchin_profile.php" ;
    ListView listView;
    ArrayList<ProfileModel> profileModels;
    ArrayAdapter modelArrayAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification_to_device);

        listView=findViewById(R.id.listview);
        profileModels=new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(SendNotificationToDevice.this);

                View customView= getLayoutInflater().inflate(R.layout.custom_dialog, null, true);
                builder.setView(customView);

                final EditText title=customView.findViewById(R.id.title);
                final EditText message=customView.findViewById(R.id.message);
                Button send=customView.findViewById(R.id.send);
                Button cancel=customView.findViewById(R.id.cancel);

                final AlertDialog alertDialog=builder.create();
                alertDialog.show();
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty())
                        {

                        }else if (title.getText().toString().isEmpty() && message.getText().toString().isEmpty()){
                            title.setError("Empty field");
                            message.setError("Empty field");
                        }else if (title.getText().toString().isEmpty()){
                            title.setError("Empty field");
                        }else {
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
        });

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
            if (jsonStr != null) {
                try {

                    JSONArray jsonArray=new JSONArray(jsonStr);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String profileId=jsonObject.getString("profile_id");
                        String profileName=jsonObject.getString("profile_name");
                        String fcmId=jsonObject.getString("fcm_id");
                        ProfileModel profileModel=new ProfileModel();
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
            modelArrayAdapter=new ArrayAdapter<ProfileModel>(SendNotificationToDevice.this,android.R.layout.activity_list_item,profileModels);
            listView.setAdapter(modelArrayAdapter);
        }
    }

}
