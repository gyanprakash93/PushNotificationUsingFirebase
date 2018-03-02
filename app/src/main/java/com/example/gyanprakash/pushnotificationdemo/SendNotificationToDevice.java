package com.example.gyanprakash.pushnotificationdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Xeper on 3/2/2018.
 */

class SendNotificationToDevice extends AppCompatActivity {
    private static final String UPLOAD_URL ="http://192.168.0.24/push_notification_demo/fetchin_profile.php" ;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification_to_device);

        listView=findViewById(R.id.listview);

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

                    JSONObject jsonobject = new JSONObject(jsonStr);
                    status = jsonobject.getString("status");
                    Log.e("jsonarray1", jsonStr);
                    if (Integer.parseInt(status) == 1) {
                        count = jsonobject.getString("notification_count");

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
            if (Integer.parseInt(status) == 1) {
                MenuItemCompat.setActionView(item, R.layout.actionbar_badge_layout);
                RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
                LinearLayout notificationcount = (LinearLayout) notifCount.findViewById(R.id.notificationcount);
                notificationcount.setVisibility(View.VISIBLE);
                TextView tv = (TextView) notifCount.findViewById(R.id.badge_textView);
                tv.setText(count);
            }
            View actionView = MenuItemCompat.getActionView(item);
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}
