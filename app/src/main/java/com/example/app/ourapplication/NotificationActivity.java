package com.example.app.ourapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.util.Helper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


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



    public void NotifyFeed(final Person person) {

        final String NOTIFICATION_CHANNEL_ID = person.getPostId();
        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Log.d(TAG, "Notif id is:  " + notifid);

        long msgtime = Helper.convertoTimeStamp(person.getTimeMsg());
        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(mContext, DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,notifid , notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        final Notification notif = new Notification.Builder(mContext,NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle(person.getMessage())
                .setContentText("New Topic by " + person.getSenderName())
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mickey))

                .setContentIntent(pendingIntent)
                .setGroup(person.getPostId())
                .build();

        Handler uiHandler = new Handler(Looper.getMainLooper());
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
                                //mBuilder.setLargeIcon(bitmap);
                               // notificationManager.notify(1, mBuilder.build());
                                notificationManager.notify(person.getPostId(), 0, notif);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                //notificationManager.notify(1, mBuilder.build());
                                notificationManager.notify(person.getPostId(), 0, notif);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
            }
        });


       // final RemoteViews contentView = notif.contentView;
       // final int iconId = R.drawable.mickey;
        // Use Picasso with RemoteViews to load image into a notification
//        Picasso.with(mContext).load(person.getPhotoId()).into(contentView, 0, iconId, notif);


    }


    public void NotifyChat(final Person person, String mTitle) {


        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Log.d(TAG, "Notif id is:  " + notifid);

        long msgtime = Helper.convertoTimeStamp(person.getTimeMsg());
        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(mContext, DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, notifid, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);



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


        notificationManager.notify(person.getPostId(), 1, summ_notif);

    }






    public void NotifyMessagingStyleNotification(List<Person> persons, Person person, String title) {
          final String NOTIFICATION_CHANNEL_ID = person.getPostId();

        int notifid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Log.d(TAG, "Notif id is:  " + notifid);
        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent notificationIntent = new Intent(mContext, DiscussionActivity.class);
        notificationIntent.putExtra("person", person);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, notifid, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                Notification.MessagingStyle messagingStyle = buildMessageList(persons, person,title);
                Notification notification = new Notification.Builder(mContext,NOTIFICATION_CHANNEL_ID)
                        .setStyle(messagingStyle)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mickey))
                        .build();
                notificationManager.notify(person.getPostId(), 1, notification);
            }

            private Notification.MessagingStyle buildMessageList(List<Person> persons, Person prsn , String topic) {



                final String MY_DISPLAY_NAME = "Me";
                Notification.MessagingStyle messagingStyle =
                        new Notification.MessagingStyle(MY_DISPLAY_NAME)
                                .setConversationTitle(topic);

                for (Person person : persons) {
                   // String sender = message.sender().equals(MY_DISPLAY_NAME) ? null : message.sender();
                    //long msgtime = Helper.convertoTimeStamp(person.getTimeMsg());
                    messagingStyle.addMessage(new Notification.MessagingStyle.Message(person.getMessage(), get_msgtime(person), person.getSenderName()));

                }
                messagingStyle.addMessage(new Notification.MessagingStyle.Message(prsn.getMessage(), get_msgtime(prsn), prsn.getSenderName()));
                return messagingStyle;
            }

    public long get_msgtime(Person p) {
      //  Helper helper = new Helper();
        long msg_time = Helper.convertoTimeStamp(p.getTimeMsg());

        return msg_time;
    }


}








