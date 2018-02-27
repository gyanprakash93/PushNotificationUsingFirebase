package com.example.gyanprakash.pushnotificationdemo;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

//        new PaymentStatusAsync().execute(regId);
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
}
