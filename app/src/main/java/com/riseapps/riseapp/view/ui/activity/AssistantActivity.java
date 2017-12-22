package com.riseapps.riseapp.view.ui.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.Utils;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.Pojo.PersonalAlarm;

import java.util.ArrayList;
import java.util.Calendar;

public class AssistantActivity extends AppCompatActivity {

    int hour, minute;
    boolean vibrate;
    TextView textView;
    private Utils utils = new Utils();
    private AlarmCreator alarmCreator = new AlarmCreator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);
        textView = findViewById(R.id.text);

        ArrayList<PersonalAlarm> alarms = utils.getPersonalAlarms(this);
        if (alarms==null){
            alarms=new ArrayList<>();
        }

        Intent intent = getIntent();
        if (AlarmClock.ACTION_SET_ALARM.equals(intent.getAction())) {
            hour = intent.getIntExtra(AlarmClock.EXTRA_HOUR, 0);
            minute = intent.getIntExtra(AlarmClock.EXTRA_MINUTES, 0);
            alarms.add(alarmCreator.getPersonalAlarm(RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM), hour, minute, true, false));
            utils.savePersonalAlarms(this, alarms);

            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            String am_pm="";
            if(calendar.get(Calendar.AM_PM)==0)
                am_pm=" am";
            else
                am_pm=" pm";
            textView.setText(String.valueOf(new TimeToView().getTimeAsText(calendar.get(Calendar.HOUR),minute)+am_pm));
        }
    }

}
