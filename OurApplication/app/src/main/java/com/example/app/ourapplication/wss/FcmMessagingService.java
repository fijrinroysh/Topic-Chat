package com.example.app.ourapplication.wss;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.app.ourapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService extends FirebaseMessagingService implements AppCompatActivity {
    public static String TAG = "FcmMessagingService";


    @Override
    public void onNewToken(String token) {  //Method is called when new token is created
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREFERENCE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN), token);
        editor.commit();

        Log.d(TAG, token+ " - Token created ");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        Log.d(TAG, title);
        Log.d(TAG, message);
        Toast.makeText(FcmMessagingService.this, title, Toast.LENGTH_SHORT).show();
        Toast.makeText(FcmMessagingService.this, message, Toast.LENGTH_SHORT).show();

    }




}