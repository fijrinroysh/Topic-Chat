package com.example.app.ourapplication.wss;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.app.ourapplication.FcmTokenService;
import com.example.app.ourapplication.R;
import com.example.app.ourapplication.pref.PreferenceEditor;
import com.example.app.ourapplication.wss.FCMInterface;
import com.example.app.ourapplication.wss.WebSocketListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FcmMessagingService extends FirebaseMessagingService  {


    public static String TAG = "FcmMessagingService";
    public FcmTokenService mFcmTokenService = new FcmTokenService(this);
    //FCMInterface mFCMInterface;

    private LocalBroadcastManager broadcaster;
    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }
    //private ArrayList<WebSocketListener> mWebSocketListeners = new ArrayList<>();
 //public DiscussionActivity mDiscussionActivity = new DiscussionActivity();


    // constructor

    public FcmMessagingService(){
//this.mFCMInterface = null;

    }

/*
    FcmMessagingService(FCMInterface ml) {
        //Setting the listener
        this.mFCMInterface = ml;
    }
 public void setFCMInterface(FCMInterface intf) {


        this.mFCMInterface = intf;
        //addFCMInterface(intf);
    }

*/
// Someone who says "Hello"
/*

    //Here Activity register to the service as Callbacks client
    public void registerClient(Activity mFCMInterface){
        this.mFCMInterface = (FCMInterface)mFCMInterface;
    }

    private List<FCMInterface> listeners = new ArrayList<>();

    public void addListener(FCMInterface toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(FCMInterface toAdd) {
        listeners.remove(toAdd);
    }

*/






    @Override
    public void onNewToken(String token) {  //Method is called when new token is created

        mFcmTokenService.storeGCMToken(token);
        Log.d(TAG, token + " - Token created ");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> message = remoteMessage.getData();
        Log.d(TAG, message.toString());

        Intent intent = new Intent("chatevent");
        //Bundle bundle = new Bundle;
        //bundle.putSerializable("data", remoteMessage);
        intent.putExtra("postid", remoteMessage.getData().get("postid"));
        intent.putExtra("userid", remoteMessage.getData().get("userid"));
        intent.putExtra("type", remoteMessage.getData().get("type"));
        intent.putExtra("time", remoteMessage.getData().get("time"));
        intent.putExtra("message", remoteMessage.getData().get("message"));
        broadcaster.sendBroadcast(intent);
       // Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
       // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        //nFCMInterface =new FCMInterface();


          //  mDiscussionActivity.onMessageReceived(remoteMessage);
        // Notify everybody that may be interested.
        /*for (FCMInterface hl : listeners) {
            hl.onMessage(remoteMessage);
            Log.d(TAG, message.toString());
        }*/


        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Title")
                .setContentText(message.toString())
                .setSmallIcon(R.mipmap.app_icon)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(/*notification id*/0, notification);


    }


}




