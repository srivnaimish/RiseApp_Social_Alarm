package com.riseapps.riseapp.executor;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.riseapps.riseapp.model.MyApplication;

import static com.riseapps.riseapp.Components.AppConstants.UPDATE_PENDING;

public class DoneReceiver extends BroadcastReceiver {

    private int id;
    private Context c;

    @Override
    public void onReceive(Context context, Intent intent) {
        id=Integer.parseInt(intent.getStringExtra("Message_ID"));
        c=context;
        NotificationManager nManager = ((NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancel(id);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((MyApplication)c.getApplicationContext()).getDatabase().chatDao().updatePendingStatus(id,true);
            }
        });
    }
}
