package com.riseapps.riseapp.view.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.OnSwipeTouchListener;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.Pojo.PersonalAlarm;

import java.io.IOException;
import java.util.ArrayList;

public class MathWake extends AppCompatActivity implements View.OnClickListener {

    int pos = 0, n1, n2;
    MediaPlayer mediaPlayer;
    private SharedPreferenceSingelton sharedPreferenceSingelton = new SharedPreferenceSingelton();
    private ArrayList<PersonalAlarm> personalAlarms = new ArrayList<>();
    private Tasks tasks = new Tasks();
    private TimeToView timeToView = new TimeToView();
    private AlarmCreator alarmCreator = new AlarmCreator();
    private TextView ans, number1, number2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (tasks.getCurrentTheme(this) == 0) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);

        int id = getIntent().getIntExtra("ID", 0);
        ArrayList<PersonalAlarm> alarms = tasks.getPersonalAlarms(this);
        if (alarms != null) {
            personalAlarms = alarms;
        }

        for (int i = 0; i < personalAlarms.size(); i++) {
            if (personalAlarms.get(i).getId() == id) {
                pos = i;
                break;
            }
        }

        boolean repeating = personalAlarms.get(pos).isRepeat();
        boolean[] repeat_days = personalAlarms.get(pos).getRepeatDays();
        long alarmTimeInMillis = personalAlarms.get(pos).getAlarmTimeInMillis();

        if (repeating) {
            if (repeat_days[timeToView.getCurrentDay()]) {
                setContentView(R.layout.activity_math);
                ringAlarmToday();
                Toast.makeText(this, "today is set\nAlso Setting for tomorrow", Toast.LENGTH_SHORT).show();
                alarmTimeInMillis = alarmTimeInMillis + (1000 * 60 * 60 * 24);
                alarmCreator.setNewAlarm(this, alarmTimeInMillis, id);
                personalAlarms.get(pos).setAlarmTimeInMillis(alarmTimeInMillis);
            } else {
                Toast.makeText(this, "today is not set\nSetting for tomorrow", Toast.LENGTH_SHORT).show();
                alarmTimeInMillis = alarmTimeInMillis + (1000 * 60 * 60 * 24);
                alarmCreator.setNewAlarm(this, alarmTimeInMillis, id);
                personalAlarms.get(pos).setAlarmTimeInMillis(alarmTimeInMillis);
                finish();
            }
        } else {
            setContentView(R.layout.activity_math);
            ringAlarmToday();
            Toast.makeText(this, "today is set", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        tasks.savePersonalAlarms(this, personalAlarms);
        if (personalAlarms.get(pos).isVibrate()) {
            assert ((Vibrator) getSystemService(VIBRATOR_SERVICE)) != null;
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).cancel();
        }
        super.onDestroy();
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

    public void ringAlarmToday() {
        ans = findViewById(R.id.ans);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        initiallizeNumbers();

        for (int i = 0; i < 12; i++) {
            findViewById(AppConstants.numberButtons[i]).setOnClickListener(this);
        }
        if (personalAlarms.get(pos).isVibrate())
            vibrate();

        ringTone();

        ans.setOnTouchListener(new OnSwipeTouchListener(MathWake.this) {
            public void onSwipeRight() {

                //Snooze
            }

            public void onSwipeLeft() {

                String answer = ans.getText().toString();
                if (answer.length() != 0 && (n1 + n2) == Integer.parseInt(answer)) {

                    if (!personalAlarms.get(pos).isRepeat()) {
                        personalAlarms.get(pos).setStatus(false);
                        //new AlarmCreator().setNewAlarm(SimpleWake.this,personalAlarms.get(pos).getAlarmTimeInMillis(),personalAlarms.get(pos).getId());
                    }
                    finish();
                } else {
                    Toast.makeText(MathWake.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                }
                //stop
            }

        });
    }

    private void initiallizeNumbers() {
        n1 = tasks.getRandomInteger();
        n2 = tasks.getRandomInteger();
        number1.setText(String.valueOf(n1));
        number2.setText(String.valueOf(n2));
    }

    private void vibrate() {
        long[] pattern = {0, 800, 800};
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createWaveform(pattern, 0));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(pattern, 0);
        }
    }

    private void ringTone() {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.n0:
                String s = ans.getText().toString() + "0";
                ans.setText(s);
                break;
            case R.id.n1:
                s = ans.getText().toString() + "1";
                ans.setText(s);
                break;
            case R.id.n2:
                s = ans.getText().toString() + "2";
                ans.setText(s);
                break;
            case R.id.n3:
                s = ans.getText().toString() + "3";
                ans.setText(s);
                break;
            case R.id.n4:
                s = ans.getText().toString() + "4";
                ans.setText(s);
                break;
            case R.id.n5:
                s = ans.getText().toString() + "5";
                ans.setText(s);
                break;
            case R.id.n6:
                s = ans.getText().toString() + "6";
                ans.setText(s);
                break;
            case R.id.n7:
                s = ans.getText().toString() + "7";
                ans.setText(s);
                break;
            case R.id.n8:
                s = ans.getText().toString() + "8";
                ans.setText(s);
                break;
            case R.id.n9:
                s = ans.getText().toString() + "9";
                ans.setText(s);
                break;
            case R.id.bck:
                s = ans.getText().toString();
                if (s.length() != 0) {
                    s = s.substring(0, s.length() - 1);
                    ans.setText(s);
                }
                break;
            case R.id.clear_all:
                ans.setText("");
                break;
        }
    }
}
