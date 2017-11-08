package com.riseapps.riseapp.executor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.riseapps.riseapp.view.activity.MathWake;
import com.riseapps.riseapp.view.activity.SimpleWake;

import java.util.Calendar;

/**
 * Created by naimish on 2/11/17.
 */

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id=intent.getIntExtra("ID",0);
        Intent intent1=new Intent(context, MathWake.class);
        intent1.putExtra("ID",id);
        context.startActivity(intent1);

    }
}
