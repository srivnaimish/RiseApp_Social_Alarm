package com.riseapps.riseapp.executor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.riseapps.riseapp.view.ui.activity.MathWake;
import com.riseapps.riseapp.view.ui.activity.SimpleWake;

/**
 * Created by naimish on 2/11/17.
 */

public class AlarmReciever extends BroadcastReceiver {

    private Intent intent1;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("ID", 0);
        if (new SharedPreferenceSingelton().getSavedInt(context, "Method") == 0) {
            intent1 = new Intent(context, SimpleWake.class);
        } else {
            intent1 = new Intent(context, MathWake.class);
        }
        intent1.putExtra("ID", id);
        context.startActivity(intent1);

    }
}
