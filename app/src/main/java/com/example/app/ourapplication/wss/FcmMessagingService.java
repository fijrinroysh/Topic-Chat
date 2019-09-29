package com.example.app.ourapplication.wss;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.app.ourapplication.R;

import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.pref.PreferenceEditor;
import com.example.app.ourapplication.rest.model.response.Person;
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

    public static String TAG = "FcmMessagingService";
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
        Log.d(TAG, message.toString());
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
        broadcaster.sendBroadcast(intent);
        mDBHelper.insertCommentData(person);
        Notifychatsummary(person);
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


    public void Notifychat(String topic, String message) {

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(topic)
                .setContentText(message)
                .setSmallIcon(R.mipmap.app_icon)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(/*notification id*/0, notification);
    }


    private void Notifychats(final Person person) {


        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Log.d(TAG, "Notif id is:  " +notifid);
        /*try {
            notifid = Integer.parseInt(person.getPostId().replaceAll("^\"|\"$", ""));
        } catch(NumberFormatException nfe) {
            Log.d(TAG, "Could not parse " + nfe);
            Log.d(TAG, "Notif id is:  " + person.getPostId().replaceAll("^\"|\"$", ""));

        }*/

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(getApplicationContext(), DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Bitmap bitmap = Helper.getBitmapFromURL(notificationIcon);


        final Notification.Builder mBuilder = new Notification.Builder(getApplicationContext())
                .setAutoCancel(true)
                .setContentTitle(person.getSenderName())
                .setContentText(person.getMessage())
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mickey))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(person.getMessage()))
                        // .setSmallIcon(setImageBitmap(Helper.decodeImageString(notificationIcon)))
                        //.setGroup(person.getPostId())
                .setContentIntent(pendingIntent);
        notificationManager.notify(notifid, mBuilder.build());


        // hide the notification after its selected
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;

      /*  Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                Picasso.with(getApplicationContext())
                        .load(person.getPhotoId())
                        .placeholder(R.drawable.mickey)
                        .error(R.drawable.mickey)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                mBuilder.setLargeIcon(bitmap);
                                notificationManager.notify(1, mBuilder.build());
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                notificationManager.notify(1, mBuilder.build());
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
            }
        });*/

        /*
        Picasso.with(getApplicationContext())
                .load(person.getPhotoId())
                .placeholder(R.drawable.mickey)
                .error(R.drawable.mickey)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mBuilder.setLargeIcon(bitmap);
                        notificationManager.notify(0, mBuilder.build());
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        notificationManager.notify(0, mBuilder.build());
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });*/
    }




    private void Notifychatsummary(final Person person) {


        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Log.d(TAG, "Notif id is:  " +notifid);


        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(getApplicationContext(), DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Bitmap bitmap = Helper.getBitmapFromURL(notificationIcon);

        Notification newMessageNotification = new NotificationCompat.Builder(getApplicationContext())
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(person.getSenderName())
                .setContentText(person.getMessage())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mickey))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setGroup(person.getPostId())
                .setContentIntent(pendingIntent)
                .build();


        Notification summaryNotification =
                new NotificationCompat.Builder(getApplicationContext())

                                //set content text to support devices running API level < 24
                        .setSmallIcon(R.mipmap.app_icon)
                                //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setSummaryText(mDBHelper.getFeedDataColumn(person.getPostId(), 3))//select the feed message to set as summary
                        )
                                //specify which group this notification belongs to
                        .setGroup(person.getPostId())
                        .setContentIntent(pendingIntent)
                                //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();
        notificationManager.notify(notifid,newMessageNotification);
        notificationManager.notify(person.getPostId(),0, summaryNotification);

    }


}




