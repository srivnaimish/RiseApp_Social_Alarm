package com.riseapps.riseapp.Components;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by naimish on 25/9/17.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "Reminders";
    public static final String ANDROID_CHANNEL_NAME = "Reminder Recieved";

    public static final String ANDROID_CHANNEL_ID1 = "Reminder Trigger";
    public static final String ANDROID_CHANNEL_NAME1 = "Reminder Triggered";

    public NotificationUtils(Context base,int choice) {
        super(base);
        if(choice==0){
            createChannel1();
        }else {
            createChannel2();
        }

    }

    public void createChannel1() {

        // create android channel
        NotificationChannel androidChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.RED);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(androidChannel);
        }

    }

    public void createChannel2() {

        // create android channel
        NotificationChannel androidChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID1,
                    ANDROID_CHANNEL_NAME1, NotificationManager.IMPORTANCE_HIGH);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.RED);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(androidChannel);
        }

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Notification.Builder getChannelNotification(String title, Bitmap largeIcon) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, i, PendingIntent.FLAG_ONE_SHOT);
        return new Notification.Builder(getApplicationContext())
                .setContentIntent(resultPendingIntent)
                .setContentTitle("You have new reminders")
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setChannelId(ANDROID_CHANNEL_ID);
    }

    public Notification.Builder getReminderNotification(ArrayList<String> details, Bitmap largeIcon) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, i, PendingIntent.FLAG_ONE_SHOT);

        Notification.BigTextStyle bigText=new Notification.BigTextStyle();
        bigText.bigText(details.get(1));
        bigText.setBigContentTitle(details.get(0));
        if (details.get(2) != null)
            bigText.setSummaryText("See Image");

        return new Notification.Builder(getApplicationContext())
                .setContentIntent(resultPendingIntent)
                .setContentTitle(details.get(0))
                .setContentText(details.get(1))
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setStyle(bigText)
                .setChannelId(ANDROID_CHANNEL_ID1);
    }

}