package com.riseapps.riseapp.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.utils.NotificationUtils;
import com.riseapps.riseapp.view.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by naimish on 25/9/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG="FIREBASE Service";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String sender = data.get("Sender");
        long time = Long.parseLong(data.get("Time"));
        String note = data.get("Note");
        String image = data.get("Image");
        bitmap = getBitmapfromUrl(image);

        sendNotification(sender+" send you a Reminder", bitmap);
    }

    private void sendNotification(String title, Bitmap image) {
    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        int NOTIFICATION_ID = 1;
        if(Build.VERSION.SDK_INT>= 26) {
            NotificationUtils mNotificationUtils = new NotificationUtils(this);
            Notification.Builder nb = mNotificationUtils.
                    getChannelNotification(title, largeIcon,image);
            mNotificationUtils.getManager().notify(NOTIFICATION_ID, nb.build());
        }else {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, i, PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setGroupSummary(true);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentTitle(title);
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(image));
            mBuilder.setOngoing(false);
            mBuilder.setSound(defaultSoundUri);
            mBuilder.setSmallIcon(R.drawable.ic_alarm_clock);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setVibrate(new long[]{100, 100});
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        /*try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            imageUrl="https://cdn.pixabay.com/photo/2017/09/12/23/14/time-2743994_640.jpg";
            getBitmapfromUrl(imageUrl);

        }

        return null;*/
        Bitmap theBitmap = null;
        try {
            URL url = new URL(imageUrl);
            try {
                theBitmap = Glide.
                        with(this)
                        .load(imageUrl)
                        .asBitmap()
                        .into(320, 100). // Width and height
                        get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            try {
                theBitmap = Glide.
                        with(this)
                        .load("https://cdn.pixabay.com/photo/2017/09/12/23/14/time-2743994_640.jpg")
                        .asBitmap()
                        .into(640, 320). // Width and height
                        get();
            } catch (InterruptedException e1) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e.printStackTrace();
            }
        }

        return theBitmap;
    }
}
