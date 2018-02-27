package com.example.gyanprakash.pushnotificationdemo;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by GyanPrakash on 2/23/2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String UPLOAD_URL ="http://127.0.0.1/push_notification_demo/fcm_token.php" ;
    public static final String SHARED_PREF = "ah_firebase";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        storeRegIdInPref(refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
//        new PaymentStatusAsync().execute(token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
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
//            Log.e("response",jsonStr);
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

}
