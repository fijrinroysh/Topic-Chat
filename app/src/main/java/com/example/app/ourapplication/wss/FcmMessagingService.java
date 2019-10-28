package com.example.app.ourapplication.wss;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.app.ourapplication.DiscussionActivity;
import com.example.app.ourapplication.FcmTokenService;
import com.example.app.ourapplication.Keys;
import com.example.app.ourapplication.LoginActivity;
import com.example.app.ourapplication.NotificationActivity;
import com.example.app.ourapplication.R;

import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.pref.PreferenceEditor;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.util.Helper;
import com.example.app.ourapplication.wss.FCMInterface;
import com.example.app.ourapplication.wss.WebSocketListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FcmMessagingService extends FirebaseMessagingService  {


    private DBHelper mDBHelper = new DBHelper(this);
    private NotificationActivity mNotificationActivity = new NotificationActivity(this);
    public static String TAG = FcmMessagingService.class.getSimpleName();
    public FcmTokenService mFcmTokenService = new FcmTokenService(this);
    private LoginActivity mLoginActivity = new LoginActivity();



    //FCMInterface mFCMInterface;
   // private Context mContext = getApplicationContext();

    private LocalBroadcastManager broadcaster;
    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }
    //private ArrayList<WebSocketListener> mWebSocketListeners = new ArrayList<>();
 //public DiscussionActivity mDiscussionActivity = new DiscussionActivity();




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
        mLoginActivity.UpdateToken(PreferenceEditor.getInstance(this).getLoggedInUserName(), mFcmTokenService.getGCMToken());

        Log.d(TAG, token + " - Token created and stored in preferences");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> message = remoteMessage.getData();
        Log.d(TAG, "Message type received in FCM: " + remoteMessage.getData().get("type"));
        if (remoteMessage.getData().get("type").trim().equals("F")){
            Person person = new Person(
                    remoteMessage.getData().get("type"),
                    remoteMessage.getData().get("postid"),
                    remoteMessage.getData().get("userid"),
                    remoteMessage.getData().get("name"),
                    remoteMessage.getData().get("message"),
                    remoteMessage.getData().get("profileimage"),
                    remoteMessage.getData().get("mediaurl"),
                    remoteMessage.getData().get("time"),
                    ""
            );
            Intent intent = new Intent("feedevent");
            intent.putExtra("person", person);
            if(mDBHelper.insertFeedData(person)){
                broadcaster.sendBroadcast(intent);
                Log.d(TAG, "Feed Message broadcasted: " + remoteMessage.getData().get("message"));
                if(!remoteMessage.getData().get("userid").equals(PreferenceEditor.getInstance(this).getLoggedInUserName())){

                    mNotificationActivity.NotifyFeed(person);
                    Log.d(TAG, "Feed Message notified: " + remoteMessage.getData().get("message"));
                }
            } ;


            //mDBHelper.getFeedDataColumn(person.getPostId(), 3)

        }else {
            Person person = new Person(
                    remoteMessage.getData().get("type"),
                    remoteMessage.getData().get("postid"),
                    remoteMessage.getData().get("userid"),
                    remoteMessage.getData().get("name"),
                    remoteMessage.getData().get("message"),
                    remoteMessage.getData().get("profileimage"),
                    "",
                    remoteMessage.getData().get("time"),
                    ""
            );


            Intent intent = new Intent("chatevent");
            intent.putExtra("person", person);
            Log.d(TAG, person.toString());
            List<Person> oldmessages = getoldMessages(person);
            if(mDBHelper.insertCommentData(person)){
                broadcaster.sendBroadcast(intent);
                Log.d(TAG, "Chat Message broadcasted: " + remoteMessage.getData().get("message"));
                if(!remoteMessage.getData().get("userid").equals(PreferenceEditor.getInstance(this).getLoggedInUserName())){
                    mNotificationActivity.NotifyMessagingStyleNotification(oldmessages, person, mDBHelper.getFeedDataColumn(person.getPostId(), 3));
                    Log.d(TAG, "Chat Message notified: " + remoteMessage.getData().get("message"));
                    }
            }

            ;
          // mNotificationActivity.NotifyMessagingStyleNotification(person, mDBHelper.getFeedDataColumn(person.getPostId(), 3));
          //  mNotificationActivity.sendMessagingStyleNotification(person);
        }

        /*Bundle bundle = new Bundle;
        //bundle.putSerializable("data", remoteMessage);
        intent.putExtra("postid", remoteMessage.getData().get("postid"));
        intent.putExtra("userid", remoteMessage.getData().get("userid"));
        intent.putExtra("type", remoteMessage.getData().get("type"));
        intent.putExtra("time", remoteMessage.getData().get("time"));
        intent.putExtra("message", remoteMessage.getData().get("message"));

       // Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
       // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        //nFCMInterface =new FCMInterface();


          //  mDiscussionActivity.onMessageReceived(remoteMessage);
        // Notify everybody that may be interested.
        for (FCMInterface hl : listeners) {
            hl.onMessage(remoteMessage);
            Log.d(TAG, message.toString());
        }*/





    }


    @NonNull
    private List<Person> getoldMessages(Person prsn){

        List<Person> messages = mDBHelper.getCommentData(prsn.getPostId());  //add messagees from db here
        //saveMessages(messages); //Not required - we have saved messages to db already
        return messages;
    }

}




