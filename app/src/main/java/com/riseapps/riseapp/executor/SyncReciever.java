package com.riseapps.riseapp.executor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.riseapps.riseapp.model.MyApplication;

/**
 * Created by naimish on 14/12/17.
 */

public class SyncReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ContactsSync contactsSync = new ContactsSync(context.getContentResolver(),((MyApplication)context.getApplicationContext()).getDatabase());
        contactsSync.execute();
    }
}
