package com.riseapps.riseapp.model.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.model.Pojo.ContactFetch;
import com.riseapps.riseapp.Components.NotificationUtils;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.util.List;
import java.util.Map;

import static com.riseapps.riseapp.Components.AppConstants.RECEIVED_MESSAGE;

/**
 * Created by naimish on 25/9/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "FIREBASE Service";
    //Bitmap bitmap;
    SharedPreferenceSingelton sharedPreferenceSingelton;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        /*String type = data.get("Type");
        if(type.equalsIgnoreCase("clear")){
            Glide.get(this).clearDiskCache();
            if(appInForeGround()){
                ((MyApplication)getApplicationContext()).clearMemory();
            }else {
                sharedPreferenceSingelton=new SharedPreferenceSingelton();
                sharedPreferenceSingelton.saveAs(this,"Clear",true);
            }

            Log.d("Cache","Cleared");
        }else {*/
            int sender_no = Integer.parseInt(data.get("Sender Index"));
            String sender_name = data.get("Sender Name");
            String sender_phone = data.get("Sender Phone");
            long time = Long.parseLong(data.get("Time"));
            String note = data.get("Note");
            String image = data.get("Image");

            insertChatMessage(sender_no, sender_name, sender_phone, time, note, image);
        //}

    }

    private void insertChatMessage(int sender_no, String sender_name, String phone, long time, String note, String image) {
        MyDB myDB = ((MyApplication) getApplicationContext()).getDatabase();

        String name;
        if (myDB.contactDao().isContactPresent(phone) == 0) {   //If phone not present in DB
            String contactName=getContactNameByNumber(phone);
            if(contactName!=null){  // If phone present in current phone contacts
                name=contactName;
            }else {     //If phone present nowhere
                name=sender_name;
            }
        }else {     //if present fetch id and name
            ContactFetch contactFetched= myDB.contactDao().getContact(phone);
            name=contactFetched.getContact_name();
        }
        if(!appInForeGround()){
            sendNotification(sender_no,name+" sent you a Reminder");
        }

        Chat_Entity chat_entity = new Chat_Entity();
        chat_entity.setMessage_id((int) System.currentTimeMillis());
        chat_entity.setChat_id(phone);
        chat_entity.setContact_name(name);
        chat_entity.setTime(time);
        chat_entity.setNote(note);
        chat_entity.setImage(image);
        chat_entity.setSent_or_recieved(RECEIVED_MESSAGE);
        chat_entity.setRead(false);
        myDB.chatDao().insertChat(chat_entity);

        ChatSummary chatSummary=new ChatSummary();
        chatSummary.setChat_contact_name(name);
        chatSummary.setChat_contact_number(phone);
        chatSummary.setRead(false);
        myDB.chatDao().insertSummary(chatSummary);

        new AlarmCreator().setNewReminder(this,time,chat_entity.getMessage_id(),name,note,image);

    }

    private void sendNotification(int notification_id, String title) {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationUtils mNotificationUtils = new NotificationUtils(this,0);
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
            mBuilder.setSmallIcon(R.drawable.ic_notification);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setVibrate(new long[]{100, 100});
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(notification_id, mBuilder.build());
            }
        }
    }

    public String getContactNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = null;

        ContentResolver contentResolver = getContentResolver();
        Cursor contact = contentResolver.query(uri, new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME_PRIMARY}, null, null, null);

        if (contact != null && contact.getCount() > 0) {
            contact.moveToNext();
            name = contact.getString(contact.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            contact.close();
        }

        return name;
    }

    public boolean appInForeGround(){
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (componentInfo.getPackageName().equalsIgnoreCase("com.riseapps.riseapp")) {
            return true;
        } else {
            return false;
        }
    }

}
