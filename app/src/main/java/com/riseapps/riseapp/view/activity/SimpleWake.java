package com.riseapps.riseapp.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.OnSwipeTouchListener;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.PersonalAlarm;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class SimpleWake extends AppCompatActivity {

    ImageView drag_button;
    int pos=0;
    MediaPlayer mediaPlayer;
    private SharedPreferenceSingelton sharedPreferenceSingelton=new SharedPreferenceSingelton();
    private ArrayList<PersonalAlarm> personalAlarms=new ArrayList<>();
    private TextView time;
    private Tasks tasks=new Tasks();
    private TimeToView timeToView=new TimeToView();
    private AlarmCreator alarmCreator=new AlarmCreator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id=getIntent().getIntExtra("ID",0);
        ArrayList<PersonalAlarm> alarms=tasks.getPersonalAlarms(this);
        if(alarms!=null){
            personalAlarms=alarms;
        }

        for(int i=0;i<personalAlarms.size();i++){
            if(personalAlarms.get(i).getId()==id){
                pos=i;
                break;
            }
        }

        boolean repeating=personalAlarms.get(pos).isRepeat();
        boolean[] repeat_days=personalAlarms.get(pos).getRepeatDays();
        long alarmTimeInMillis=personalAlarms.get(pos).getAlarmTimeInMillis();

        if(repeating){
            if(repeat_days[timeToView.getCurrentDay()]){
                setContentView(R.layout.activity_simple_wake);
                ringAlarmToday();
                Toast.makeText(this, "today is set\nAlso Setting for tomorrow", Toast.LENGTH_SHORT).show();
                alarmTimeInMillis = alarmTimeInMillis + (1000 * 60 * 60 * 24);
                alarmCreator.setNewAlarm(this,alarmTimeInMillis,id);
                personalAlarms.get(pos).setAlarmTimeInMillis(alarmTimeInMillis);
            }else {
                Toast.makeText(this, "today is not set\nSetting for tomorrow", Toast.LENGTH_SHORT).show();
                alarmTimeInMillis = alarmTimeInMillis + (1000 * 60 * 60 * 24);
                alarmCreator.setNewAlarm(this,alarmTimeInMillis,id);
                personalAlarms.get(pos).setAlarmTimeInMillis(alarmTimeInMillis);
                finish();
            }
        }else {
            setContentView(R.layout.activity_simple_wake);
            ringAlarmToday();
            Toast.makeText(this, "today is set", Toast.LENGTH_SHORT).show();
        }


        /*Uri alert = Uri.parse("DEFAULT_SOUND");
        mediaPlayer = new MediaPlayer();
        //Log.d("Tone",defRingtone);
        try {
            mediaPlayer.setDataSource(this, alert);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 15, 0);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Not Allowed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        tasks.savePersonalAlarms(this,personalAlarms);
        if(personalAlarms.get(pos).isVibrate()) {
            assert ((Vibrator) getSystemService(VIBRATOR_SERVICE)) != null;
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).cancel();
        }
        super.onDestroy();
    }

    private void vibrate() {
        long[] pattern = {0, 800, 800};
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createWaveform(pattern,0));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(pattern,0);
        }
    }

    private void ringTone(){
        Uri alert = Uri.parse(personalAlarms.get(pos).getTone());
        mediaPlayer = new MediaPlayer();
        //Log.d("Tone",defRingtone);
        try {
            mediaPlayer.setDataSource(this, alert);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 15, 0);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onAttachedToWindow();
    }

    public void ringAlarmToday(){
        drag_button=findViewById(R.id.drag_button);
        time=findViewById(R.id.time);
        Calendar calendar=personalAlarms.get(pos).getCalendar();
        String timeString=new TimeToView().getTimeAsText(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
        time.setText(timeString);
        if(personalAlarms.get(pos).isVibrate())
        vibrate();

        ringTone();
        drag_button.setOnTouchListener(new OnSwipeTouchListener(SimpleWake.this) {
            public void onSwipeRight() {

                //Snooze
            }

            public void onSwipeLeft() {
                if(!personalAlarms.get(pos).isRepeat()){
                    personalAlarms.get(pos).setStatus(false);
                    //new AlarmCreator().setNewAlarm(SimpleWake.this,personalAlarms.get(pos).getAlarmTimeInMillis(),personalAlarms.get(pos).getId());
                }
                finish();
                //stop
            }

        });
    }
}

