package com.riseapps.riseapp.model.Service;

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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.model.Pojo.ContactFetch;
import com.riseapps.riseapp.utils.NotificationUtils;
import com.riseapps.riseapp.view.activity.MainActivity;
import com.riseapps.riseapp.view.activity.SendReminderActivity;

import java.util.List;
import java.util.Map;

import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.RECIEVED_MESSAGE;
import static com.riseapps.riseapp.Components.AppConstants.SENT_MESSAGE;

/**
 * Created by naimish on 25/9/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "FIREBASE Service";
    //Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        int sender_no = Integer.parseInt(data.get("Sender Index"));
        String sender_name=data.get("Sender Name");
        String sender_phone=data.get("Sender Phone");
        long time = Long.parseLong(data.get("Time"));
        String note = data.get("Note");
        String image = data.get("Image");

        sendNotification(sender_no,sender_name+" sent you a Reminder");

        insertChatMessage(sender_phone,time,note,image);

    }

    private void sendNotification(int notification_id, String title) {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationUtils mNotificationUtils = new NotificationUtils(this);
            Notification.Builder nb = mNotificationUtils.
                    getChannelNotification(title, largeIcon);
            mNotificationUtils.getManager().notify(notification_id, nb.build());
        } else {

            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, i, PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setGroupSummary(true);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentTitle("You have new reminders");
            mBuilder.setContentText(title);
            mBuilder.setOngoing(false);
            mBuilder.setSound(defaultSoundUri);
            mBuilder.setSmallIcon(R.drawable.ic_add_alarm);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setVibrate(new long[]{100, 100});
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(notification_id, mBuilder.build());
            }
        }
    }

    private void insertChatMessage(String phone,long time,String note,String image){
        MyDB myDB=((MyApplication) getApplicationContext()).getDatabase();

        if(myDB.contactDao().isContactPresent(phone)==0){   //Not found contact name-->phone number

        }else {
            ContactFetch contactFetched=myDB.contactDao().getContact(phone);

            Chat_Entity chat_entity = new Chat_Entity();
            chat_entity.setContact_id(contactFetched.getId());
            chat_entity.setContact_name(contactFetched.getContact_name());
            chat_entity.setTime(time);
            chat_entity.setNote(note);
            chat_entity.setImage(image);
            chat_entity.setSent_or_recieved(RECIEVED_MESSAGE);
            chat_entity.setRead_status(false);
            myDB.chatDao().insertChat(chat_entity);

            ChatSummary chatSummary=new ChatSummary();
            chatSummary.setChat_contact_id(contactFetched.getId());
            chatSummary.setChat_contact_name(contactFetched.getContact_name());
            chatSummary.setRead(false);
            myDB.chatDao().insertSummary(chatSummary);

        }
    }


}
