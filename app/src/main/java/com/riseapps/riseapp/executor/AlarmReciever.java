package com.riseapps.riseapp.executor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.riseapps.riseapp.Components.NotificationUtils;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.view.ui.activity.MainActivity;
import com.riseapps.riseapp.view.ui.activity.MathWake;
import com.riseapps.riseapp.view.ui.activity.ReminderActivity;
import com.riseapps.riseapp.view.ui.activity.SimpleWake;

import java.util.ArrayList;

/**
 * Created by naimish on 2/11/17.
 */

public class AlarmReciever extends BroadcastReceiver {

    private Intent intent1;

    @Override
    public void onReceive(Context context, Intent intent) {

        int type = intent.getIntExtra("Type", 0);

        if (type == 0) {
            int id = intent.getIntExtra("ID", 0);
            if (new SharedPreferenceSingelton().getSavedInt(context, "Method") == 0) {
                intent1 = new Intent(context, SimpleWake.class);
            } else {
                intent1 = new Intent(context, MathWake.class);
            }
            intent1.putExtra("ID", id);
            context.startActivity(intent1);
        } else {
            ArrayList<String> details = intent.getStringArrayListExtra("Details");
            sendNotification(context, details);
        }
    }

    private void sendNotification(Context context, ArrayList<String> details) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationUtils mNotificationUtils = new NotificationUtils(context, 1);
            Notification.Builder nb = mNotificationUtils.
                    getReminderNotification(details, largeIcon);
            mNotificationUtils.getManager().notify(0, nb.build());
        } else {

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(details.get(1));
            bigText.setBigContentTitle(details.get(0));
            if (details.get(2) != null)
                bigText.setSummaryText("See Image");

            Intent i = new Intent(context, ReminderActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 2, i, PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setGroupSummary(true);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentTitle("RiseApp Reminder");
            mBuilder.setContentText("Read the reminder");
            mBuilder.setOngoing(false);
            mBuilder.setSound(defaultSoundUri);
            mBuilder.setSmallIcon(R.drawable.ic_add_alarm);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setStyle(bigText);
            mBuilder.setVibrate(new long[]{80, 80});
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }

}
