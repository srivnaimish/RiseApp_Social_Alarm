package com.riseapps.riseapp.executor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.riseapps.riseapp.model.PersonalAlarm;

import java.util.Calendar;

/**
 * Created by naimish on 31/10/17.
 */

public class AlarmCreator {

    public PersonalAlarm getPersonalAlarm(Uri ringtone,int selectedHour, int selectedMinute, boolean status, boolean repeat){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        int id=selectedHour*100+selectedMinute;
        long alarmTimeInMillis = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
        if (System.currentTimeMillis() >= alarmTimeInMillis)
            alarmTimeInMillis = alarmTimeInMillis + (1000 * 60 * 60 * 24);

        boolean[] repeat_days={false,false,false,false,false,false,false};
        return new PersonalAlarm(id,calendar,status,repeat,repeat_days, alarmTimeInMillis,ringtone.toString() , true);
    }

    public void setNewAlarm(Context context,long alarmTimeInMillis,int id){

        Intent intent = new Intent(context,AlarmReciever.class);
        intent.putExtra("ID",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pi);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pi);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pi);
    }

    public void setAlarmOff(Context context,int id){

        Intent intent = new Intent(context,AlarmReciever.class);
        intent.putExtra("ID",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }

}
