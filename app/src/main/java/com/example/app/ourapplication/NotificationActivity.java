package com.example.app.ourapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.util.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity {

    private Context mContext;
    public NotificationActivity(Context context) {
        this.mContext = context;

    }
    private final String TAG = NotificationActivity.class.getSimpleName();

    private DBHelper mDBHelper = new DBHelper(mContext);






    public void Notifychatsummary(final Person person) {


        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Log.d(TAG, "Notif id is:  " +notifid);


        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(mContext, DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);



    }



    public void Notifychatinbox(final Person person, String mTitle) {


        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Log.d(TAG, "Notif id is:  " +notifid);


        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(mContext, DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Bitmap bitmap = Helper.getBitmapFromURL(notificationIcon);





        Notification notif = new Notification.Builder(mContext)
                .setAutoCancel(true)
                .setContentTitle(person.getMessage())
                .setContentText(person.getSenderName() + " invites you to the Topic")
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mickey))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .setGroupSummary(true)
                .setGroup(person.getPostId())
                .setStyle(new Notification.InboxStyle()
                        .addLine(person.getMessage())
                        .setSummaryText(mTitle))
                .build();
        notificationManager.notify(person.getPostId(), notifid, notif);

    }



    public void Notifychatmessage(final Person person, String mTitle) {


        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Log.d(TAG, "Notif id is:  " + notifid);

        long msgtime = Helper.convertoTimeStamp(person.getTimeMsg());
        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(mContext, DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        final Notification notif = new NotificationCompat.Builder(mContext)
                .setAutoCancel(true)
                .setContentTitle(person.getSenderName() )
                .setContentText(person.getMessage())
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mickey))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .setGroup(person.getPostId())
                .build();

        //MESSAGES.add(new NotificationCompat.MessagingStyle.Message(person.getMessage(),msgtime,person.getSenderName() ));
            // List<NotificationCompat.MessagingStyle.Message> MESSAGES = new ArrayList<>();


        NotificationCompat.MessagingStyle messagingStyle =
                new NotificationCompat.MessagingStyle("You");
        messagingStyle.setConversationTitle(mTitle);
        messagingStyle.addMessage((new NotificationCompat.MessagingStyle.Message(person.getMessage(), msgtime, person.getSenderName())));




        //Person user = new Person.Builder().setIcon(userIcon).setName(userName).build();
        Notification summ_notif = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mickey))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                        //.setColor(Color.BLUE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setStyle(messagingStyle)
                .setGroupSummary(true)
                .setGroup(person.getPostId())
                .build();



        notificationManager.notify(notifid,notif);
        notificationManager.notify(person.getPostId(),0,summ_notif);

    }






    public void Notifychats(final Person person) {


        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Log.d(TAG, "Notif id is:  " + notifid);
        /*try {
            notifid = Integer.parseInt(person.getPostId().replaceAll("^\"|\"$", ""));
        } catch(NumberFormatException nfe) {
            Log.d(TAG, "Could not parse " + nfe);
            Log.d(TAG, "Notif id is:  " + person.getPostId().replaceAll("^\"|\"$", ""));

        }*/

        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(mContext, DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Bitmap bitmap = Helper.getBitmapFromURL(notificationIcon);


        final Notification.Builder mBuilder = new Notification.Builder(mContext)
                .setAutoCancel(true)
                .setContentTitle(person.getSenderName()+ " in new topic")
                .setContentText(person.getMessage())
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mickey))
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
                Picasso.with(mContext)
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
        Picasso.with(mContext)
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