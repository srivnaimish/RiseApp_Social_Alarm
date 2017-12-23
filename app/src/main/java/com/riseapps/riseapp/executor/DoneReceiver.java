package com.riseapps.riseapp.executor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.riseapps.riseapp.model.MyApplication;

import static com.riseapps.riseapp.Components.AppConstants.UPDATE_PENDING;

public class DoneReceiver extends BroadcastReceiver {

    private int id;
    private Context c;

    @Override
    public void onReceive(Context context, Intent intent) {
        id=Integer.parseInt(intent.getStringExtra("Message_ID"));
        c=context;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ChatSync chatSync=new ChatSync(id,((MyApplication)c.getApplicationContext()).getDatabase(),UPDATE_PENDING);
                chatSync.execute();
            }
        });
    }
}
