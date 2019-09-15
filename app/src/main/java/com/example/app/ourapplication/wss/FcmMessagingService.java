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
import com.example.app.ourapplication.R;
import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.pref.PreferenceEditor;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.wss.FCMInterface;
import com.example.app.ourapplication.wss.WebSocketListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FcmMessagingService extends FirebaseMessagingService  {

    private DBHelper mDBHelper = new DBHelper(this);
    public static String TAG = "FcmMessagingService";
    public FcmTokenService mFcmTokenService = new FcmTokenService(this);
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
        Log.d(TAG, token + " - Token created ");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> message = remoteMessage.getData();
        Log.d(TAG, message.toString());
        Person person = new Person(
                remoteMessage.getData().get("type"),
                remoteMessage.getData().get("postid"),
                "9894231831",
                "Fijrin",
                remoteMessage.getData().get("message"),
                "urltobeaddedinserver",
                "",
                remoteMessage.getData().get("time"),
                ""
        );

        Intent intent = new Intent("chatevent");
        intent.putExtra("person", person);
        broadcaster.sendBroadcast(intent);
        mDBHelper.insertCommentData(person);
        Notifychats(person);
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

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(getApplicationContext(), DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            // Perform the operation associated with our pendingIntent
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
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
                .setContentIntent(pendingIntent);
        // hide the notification after its selected
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;

        Handler uiHandler = new Handler(Looper.getMainLooper());
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
        });

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

}




