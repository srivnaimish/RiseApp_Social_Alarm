package com.riseapps.riseapp.view.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.Interface.RingtonePicker;
import com.riseapps.riseapp.executor.Utils;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.Pojo.PersonalAlarm;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

import static com.riseapps.riseapp.Components.AppConstants.RC_RINGTONE;

public class PersonalAlarmAdapter extends RecyclerView.Adapter {

    private ArrayList<PersonalAlarm> alarms;
    private Context c;
    private TimeToView timeToView;
    private AlarmCreator alarmCreator;
    private Utils task = new Utils();
    private LinearLayout empty_state;

    public PersonalAlarmAdapter(Context context, ArrayList<PersonalAlarm> alarms, LinearLayout empty_state) {
        this.alarms = alarms;
        this.empty_state = empty_state;
        c = context;
        timeToView = new TimeToView();
        alarmCreator = new AlarmCreator();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_row, parent, false);
        return new PersonalAlarmHolder(view, c);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PersonalAlarm alarm = alarms.get(position);
        ((PersonalAlarmHolder) holder).alarm = alarm;

        PersonalAlarmHolder personalAlarmHolder=(PersonalAlarmHolder) holder;

        Calendar calendar = alarm.getCalendar();

        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int am_pm=calendar.get(Calendar.AM_PM);
        String time=timeToView.getTimeAsText(hour,minutes);
        personalAlarmHolder.time.setText(time);
        if(am_pm == Calendar.AM)
            personalAlarmHolder.am_pm.setText("am");
        else
            personalAlarmHolder.am_pm.setText("pm");

        if (alarm.isVibrate()) {
            personalAlarmHolder.vibrate.setText(R.string.on);
        } else
            personalAlarmHolder.vibrate.setText(R.string.off);

        if (alarm.isStatus())
            personalAlarmHolder.aSwitch.setChecked(true);

        if (alarm.isRepeat()) {
            personalAlarmHolder.repeat.setChecked(true);
        }

        personalAlarmHolder.days.setVisibility(View.GONE);
        personalAlarmHolder.bottom.setVisibility(View.GONE);
        personalAlarmHolder.up.setVisibility(View.GONE);


    }

    public void delete(int position) {
        alarmCreator.setAlarmOff(c, alarms.get(position).getId());
        alarms.remove(position);
        notifyDataSetChanged();
        if (alarms.size() == 0) {
            empty_state.setVisibility(View.VISIBLE);
        }
        task.savePersonalAlarms(c, alarms);
    }


    @Override
    public int getItemCount() {
        return alarms.size();
    }

    private class PersonalAlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        CardView alarmCard;


        TextView time, am_pm;
        Switch aSwitch;
        CheckBox repeat;
        Button[] repeat_days = new Button[7];
        LinearLayout days, bottom;
        Button sound, vibrate, delete;
        ImageButton up;


        private Context ctx;
        PersonalAlarm alarm;

        PersonalAlarmHolder(View v, Context context) {
            super(v);
            this.ctx = context;
            time = (TextView) v.findViewById(R.id.time);
            am_pm=v.findViewById(R.id.am_pm);
            aSwitch = v.findViewById(R.id.aSwitch);
            repeat = v.findViewById(R.id.repeat);

            for (int i = 0; i < 7; i++) {
                repeat_days[i] = v.findViewById(AppConstants.daysButtons[i]);
                repeat_days[i].setOnClickListener(this);
            }

            aSwitch.setOnCheckedChangeListener(this);

            bottom = v.findViewById(R.id.bottom);
            days = v.findViewById(R.id.days);
            sound = v.findViewById(R.id.sound);
            vibrate = v.findViewById(R.id.vibrate);
            delete = v.findViewById(R.id.delete);
            up = v.findViewById(R.id.up);

            alarmCard = v.findViewById(R.id.alarm_list_card);

            repeat.setOnCheckedChangeListener(this);

            sound.setOnClickListener(this);
            vibrate.setOnClickListener(this);
            delete.setOnClickListener(this);
            up.setOnClickListener(this);

            ((MainActivity) c).setRingtonePicker(new RingtonePicker() {
                @Override
                public void onRingtonePicked(Uri uri) {
                    alarm.setTone(uri.toString());
                    task.savePersonalAlarms(ctx, alarms);
                }
            });

            alarmCard.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alarm_list_card:                   //card expand collapse
                    if (up.getVisibility() == View.GONE) {
                        days.setVisibility(View.VISIBLE);
                        bottom.setVisibility(View.VISIBLE);
                        up.setVisibility(View.VISIBLE);
                    } else {
                        days.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                        up.setVisibility(View.GONE);
                    }
                    break;

                case R.id.up:                           //card collapse on arrow click
                    days.setVisibility(View.GONE);
                    bottom.setVisibility(View.GONE);
                    up.setVisibility(View.GONE);
                    break;

                case R.id.vibrate:
                    vibrate.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.vibrate));
                    if (alarm.isVibrate()) {
                        vibrate.setText(R.string.off);
                        alarm.setVibrate(false);
                    } else {
                        vibrate.setText(R.string.on);
                        alarm.setVibrate(true);
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;

                case R.id.sound:
                    Uri currentTone = RingtoneManager.getActualDefaultRingtoneUri(c, RingtoneManager.TYPE_ALARM);
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                    ((MainActivity) c).startActivityForResult(intent, RC_RINGTONE);
                    break;

                case R.id.delete:
                    delete(getAdapterPosition());
                    break;

                case R.id.sun:
                    repeat_days[0].startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.day));
                    if (alarm.getRepeatDays()[0]) {
                        repeat_days[0].setSelected(false);
                        repeat_days[0].setTextColor(ContextCompat.getColor(c,R.color.textColorPrimary1));
                        alarm.getRepeatDays()[0] = false;
                        Log.d("sun","off");
                    } else {
                        repeat_days[0].setSelected(true);
                        repeat_days[0].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                        alarm.getRepeatDays()[0] = true;
                        Log.d("sun","on");
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
                case R.id.mon:
                    repeat_days[1].startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.day));
                    if (alarm.getRepeatDays()[1]) {
                        repeat_days[1].setSelected(false);
                        repeat_days[1].setTextColor(ContextCompat.getColor(c,R.color.textColorPrimary1));
                        alarm.getRepeatDays()[1] = false;
                        Log.d("mon","off");
                    } else {
                        repeat_days[1].setSelected(true);
                        repeat_days[1].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                        alarm.getRepeatDays()[1] = true;
                        Log.d("mon","on");
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
                case R.id.tue:
                    repeat_days[2].startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.day));
                    if (alarm.getRepeatDays()[2]) {
                        repeat_days[2].setSelected(false);
                        repeat_days[2].setTextColor(ContextCompat.getColor(c,R.color.textColorPrimary1));
                        alarm.getRepeatDays()[2] = false;
                        Log.d("tue","off");
                    } else {
                        repeat_days[2].setSelected(true);
                        repeat_days[2].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                        alarm.getRepeatDays()[2] = true;
                        Log.d("tue","on");
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
                case R.id.wed:
                    repeat_days[3].startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.day));
                    if (alarm.getRepeatDays()[3]) {
                        repeat_days[3].setSelected(false);
                        repeat_days[3].setTextColor(ContextCompat.getColor(c,R.color.textColorPrimary1));
                        alarm.getRepeatDays()[3] = false;
                        Log.d("wed","off");
                    } else {
                        repeat_days[3].setSelected(true);
                        repeat_days[3].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                        alarm.getRepeatDays()[3] = true;
                        Log.d("wed","on");
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
                case R.id.thu:
                    repeat_days[4].startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.day));
                    if (alarm.getRepeatDays()[4]) {
                        repeat_days[4].setSelected(false);
                        repeat_days[4].setTextColor(ContextCompat.getColor(c,R.color.textColorPrimary1));
                        alarm.getRepeatDays()[4] = false;
                        Log.d("thu","off");
                    } else {
                        repeat_days[4].setSelected(true);
                        repeat_days[4].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                        alarm.getRepeatDays()[4] = true;
                        Log.d("thu","on");
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
                case R.id.fri:
                    repeat_days[5].startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.day));
                    if (alarm.getRepeatDays()[5]) {
                        repeat_days[5].setSelected(false);
                        repeat_days[5].setTextColor(ContextCompat.getColor(c,R.color.textColorPrimary1));
                        alarm.getRepeatDays()[5] = false;
                        Log.d("fr","off");
                    } else {
                        repeat_days[5].setSelected(true);
                        repeat_days[5].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                        alarm.getRepeatDays()[5] = true;
                        Log.d("fr","on");
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
                case R.id.sat:
                    repeat_days[6].startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.day));
                    if (alarm.getRepeatDays()[6]) {
                        repeat_days[6].setSelected(false);
                        repeat_days[6].setTextColor(ContextCompat.getColor(c,R.color.textColorPrimary1));
                        alarm.getRepeatDays()[6] = false;
                        Log.d("sat","off");
                    } else {
                        repeat_days[6].setSelected(true);
                        repeat_days[6].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                        alarm.getRepeatDays()[6] = true;
                        Log.d("sat","on");
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;


            }

        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            switch (compoundButton.getId()) {
                case R.id.repeat:
                    boolean[] repeat_days = alarm.getRepeatDays();
                    if (b) {
                        alarmCard.performClick();
                        if (!repeat_days[0]          //If no previous repeat found, set all as true
                                && !repeat_days[1]
                                && !repeat_days[2]
                                && !repeat_days[3]
                                && !repeat_days[4]
                                && !repeat_days[5]
                                && !repeat_days[6]) {

                            for (int i = 0; i < 7; i++) {
                                alarm.getRepeatDays()[i] = true;
                                this.repeat_days[i].setSelected(true);
                                this.repeat_days[i].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                            }
                        } else {
                            for (int i = 0; i < 7; i++) {
                                if (repeat_days[i]) {
                                    this.repeat_days[i].setSelected(true);
                                    this.repeat_days[i].setTextColor(ContextCompat.getColor(c,android.R.color.white));
                                }
                            }
                        }
                        alarm.setRepeat(true);  //set current alarm as repeating
                    } else {
                        alarm.setRepeat(false);
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
                case R.id.aSwitch:

                    if (b) {

                        Calendar savedCalendar = alarm.getCalendar();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, savedCalendar.get(Calendar.HOUR_OF_DAY));
                        calendar.set(Calendar.MINUTE, savedCalendar.get(Calendar.MINUTE));

                        long alarmTimeInMillis = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                        if (System.currentTimeMillis() >= alarmTimeInMillis)
                            alarmTimeInMillis = alarmTimeInMillis + (1000 * 60 * 60 * 24);

                        alarm.setAlarmTimeInMillis(alarmTimeInMillis);

                        alarmCreator.setNewAlarm(ctx, alarmTimeInMillis, alarm.getId());
                        alarm.setStatus(true);

                    } else {
                        //Toast.makeText(ctx, "UnChecked", Toast.LENGTH_SHORT).show();

                        alarmCreator.setAlarmOff(ctx, alarm.getId());
                        alarm.setStatus(false);
                    }
                    task.savePersonalAlarms(ctx, alarms);
                    break;
            }

        }
    }
}
