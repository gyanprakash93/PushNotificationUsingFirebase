package com.example.gyanprakash.pushnotificationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREF = "ah_firebase";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String UPLOAD_URL ="http://127.0.0.1/push_notification_demo/fcm_token.php" ;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private MenuItem item;

    String regId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

//        new PaymentStatusAsync().execute(regId);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("abc","abc");

                if (intent.getAction().equals(Config.COUNT_NOTIFICATION)) {
                    if(regId!=null)
                    {
                        new JSONAsynTask2().execute();
                    }
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };

        
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.COUNT_NOTIFICATION));
        if(regId!=null)
        {
            new JSONAsynTask2().execute();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        if(regId!=null)
        {
            new JSONAsynTask2().execute();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

//        if (Internet.isConnected(this)) {
//
//            new JSONAsynTask().execute();
//
//        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
//        }
        item = menu.findItem(R.id.action_notificaton);
        MenuItemCompat.setActionView(item, R.layout.actionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        LinearLayout notificationcount = (LinearLayout) notifCount.findViewById(R.id.notificationcount);
        notificationcount.setVisibility(View.GONE);
        View actionView = MenuItemCompat.getActionView(item);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return true;
    }

    public class PaymentStatusAsync extends AsyncTask<String, Void, String> {
        String result;
        String status;
        String module;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("pre","came");

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("doin","came");
            String data = null;
            try {
                data = URLEncoder.encode("method", "UTF-8")
                        + "=" + URLEncoder.encode("addtoken", "UTF-8");
                data += "&" + URLEncoder.encode("fcm_token", "UTF-8")
                        + "=" + URLEncoder.encode(params[0], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Http sh = new Http();
            String jsonStr = sh.makeServiceCall(UPLOAD_URL, data);
            Log.e("response",jsonStr);
            if (jsonStr != null) {
                Log.e("response",jsonStr);
                return "1";
            } else {
                return "400";
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("post","came");
        }
    }


    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }
    class JSONAsynTask2 extends AsyncTask<Void, Void, Boolean> {
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
                        + "=" + URLEncoder.encode("fetchingnotificationcount", "UTF-8");
                data += "&" + URLEncoder.encode("fcm_id", "UTF-8")
                        + "=" + URLEncoder.encode(regId, "UTF-8");
                Log.e("jsonarray", data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Http sh = new Http();
            jsonStr = sh.makeServiceCall("http://192.168.43.219/push_notification_demo/notificationcount.php", data);
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
