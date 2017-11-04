package com.riseapps.riseapp.executor.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.Toast;

import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.PersonalAlarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PersonalAlarmAdapter extends RecyclerView.Adapter {

    private ArrayList<PersonalAlarm> alarms;
    private Context c;
    private TimeToView timeToView;
    private AlarmCreator alarmCreator;
    private Tasks task=new Tasks();

    public PersonalAlarmAdapter(Context context, ArrayList<PersonalAlarm> alarms, RecyclerView recyclerView) {
        this.alarms = alarms;
        c = context;
        timeToView = new TimeToView();
        alarmCreator = new AlarmCreator();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_row, parent, false);
        return new FolderViewHolder(view, c);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PersonalAlarm alarm = alarms.get(position);
        ((FolderViewHolder) holder).alarm = alarm;

        Calendar calendar = alarm.getCalendar();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        Log.d("TAG", System.currentTimeMillis() + "\n" + alarm.getAlarmTimeInMillis());
        ((FolderViewHolder) holder).time.setText(timeToView.getTimeAsText(hour, minutes));
        ((FolderViewHolder) holder).day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
        ((FolderViewHolder) holder).sound.setText(alarm.getTone());
        if (alarm.isVibrate()) {
            ((FolderViewHolder) holder).vibrate.setText(R.string.on);
        }else
            ((FolderViewHolder) holder).vibrate.setText(R.string.off);

        if(alarm.isStatus())
            ((FolderViewHolder) holder).aSwitch.setChecked(true);

        if(alarm.isRepeat()){
            ((FolderViewHolder) holder).repeat.setChecked(true);
        }

    }

    public void delete(int position){
        alarmCreator.setAlarmOff(c,alarms.get(position).getId());
        alarms.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return alarms.size();
    }

    private class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        CardView alarmCard;


        TextView time, day;
        Switch aSwitch;
        CheckBox repeat;
        Button[] repeat_days = new Button[7];
        LinearLayout days, bottom;
        Button sound, vibrate,delete;
        ImageButton up;


        private Context ctx;
        PersonalAlarm alarm;

        FolderViewHolder(View v, Context context) {
            super(v);
            this.ctx = context;
            time = (TextView) v.findViewById(R.id.time);
            day = v.findViewById(R.id.next_trigger_day);
            aSwitch = v.findViewById(R.id.aSwitch);
            repeat = v.findViewById(R.id.repeat);

            for (int i = 0; i < 7; i++) {
                repeat_days[i] = v.findViewById(AppConstants.daysButtons[i]);
                repeat_days[i].setOnClickListener(this);
            }

            aSwitch.setOnCheckedChangeListener(this);

            days = v.findViewById(R.id.days);
            bottom = v.findViewById(R.id.bottom);

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
                    vibrate.startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.vibrate));
                    if (alarm.isVibrate()) {
                        vibrate.setText(R.string.off);
                        alarm.setVibrate(false);
                    } else {
                        vibrate.setText(R.string.on);
                        alarm.setVibrate(true);
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;

                case R.id.sound:
                    /*Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
                    startActivityForResult( intent, 999);*/
                    task.savePersonalAlarms(ctx,alarms);
                    break;

                case R.id.delete:
                    delete(getAdapterPosition());
                    task.savePersonalAlarms(ctx,alarms);
                    break;

                case R.id.sun:
                    repeat_days[0].startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.day));
                    day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
                    if (alarm.getRepeatDays()[0]) {
                        repeat_days[0].setBackgroundResource(R.drawable.day_off);
                        alarm.getRepeatDays()[0] = false;
                    } else {
                        repeat_days[0].setBackgroundResource(R.drawable.day_on);
                        alarm.getRepeatDays()[0] = true;
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
                case R.id.mon:
                    repeat_days[1].startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.day));
                    day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
                    if (alarm.getRepeatDays()[1]) {
                        repeat_days[1].setBackgroundResource(R.drawable.day_off);
                        alarm.getRepeatDays()[1] = false;
                    } else {
                        repeat_days[1].setBackgroundResource(R.drawable.day_on);
                        alarm.getRepeatDays()[1] = true;
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
                case R.id.tue:
                    repeat_days[2].startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.day));
                    day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
                    if (alarm.getRepeatDays()[2]) {
                        repeat_days[2].setBackgroundResource(R.drawable.day_off);
                        alarm.getRepeatDays()[2] = false;
                    } else {
                        repeat_days[2].setBackgroundResource(R.drawable.day_on);
                        alarm.getRepeatDays()[2] = true;
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
                case R.id.wed:
                    repeat_days[3].startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.day));
                    day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
                    if (alarm.getRepeatDays()[3]) {
                        repeat_days[3].setBackgroundResource(R.drawable.day_off);
                        alarm.getRepeatDays()[3] = false;
                    } else {
                        repeat_days[3].setBackgroundResource(R.drawable.day_on);
                        alarm.getRepeatDays()[3] = true;
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
                case R.id.thu:
                    repeat_days[4].startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.day));
                    day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
                    if (alarm.getRepeatDays()[4]) {
                        repeat_days[4].setBackgroundResource(R.drawable.day_off);
                        alarm.getRepeatDays()[4] = false;
                    } else {
                        repeat_days[4].setBackgroundResource(R.drawable.day_on);
                        alarm.getRepeatDays()[4] = true;
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
                case R.id.fri:
                    repeat_days[5].startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.day));
                    day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
                    if (alarm.getRepeatDays()[5]) {
                        repeat_days[5].setBackgroundResource(R.drawable.day_off);
                        alarm.getRepeatDays()[5] = false;
                    } else {
                        repeat_days[5].setBackgroundResource(R.drawable.day_on);
                        alarm.getRepeatDays()[5] = true;
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
                case R.id.sat:
                    repeat_days[6].startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.day));
                    day.setText(timeToView.getNextTriggerDay(alarm.getAlarmTimeInMillis(), alarm.getRepeatDays(), alarm.isRepeat()));
                    if (alarm.getRepeatDays()[6]) {
                        repeat_days[6].setBackgroundResource(R.drawable.day_off);
                        alarm.getRepeatDays()[6] = false;
                    } else {
                        repeat_days[6].setBackgroundResource(R.drawable.day_on);
                        alarm.getRepeatDays()[6] = true;
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;


            }

        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            switch (compoundButton.getId()){
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
                                alarm.getRepeatDays()[i]=true;
                                this.repeat_days[i].setBackgroundResource(R.drawable.day_on);
                            }
                        }else {
                            for (int i = 0; i < 7; i++) {
                                if(repeat_days[i]){
                                    this.repeat_days[i].setBackgroundResource(R.drawable.day_on);
                                }
                            }
                        }
                        alarm.setRepeat(true);  //set current alarm as repeating
                    } else {
                        alarm.setRepeat(false);
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
                case R.id.aSwitch:

                    if(b){
                        //Toast.makeText(ctx, "Checked", Toast.LENGTH_SHORT).show();

                        Calendar savedCalendar=alarm.getCalendar();

                        Calendar calendar=Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, savedCalendar.get(Calendar.HOUR_OF_DAY));
                        calendar.set(Calendar.MINUTE, savedCalendar.get(Calendar.MINUTE));

                        long alarmTimeInMillis = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                        if (System.currentTimeMillis() >= alarmTimeInMillis)
                            alarmTimeInMillis = alarmTimeInMillis + (1000 * 60 * 60 * 24);

                        alarm.setAlarmTimeInMillis(alarmTimeInMillis);
                        day.setText(timeToView.getNextTriggerDay(alarmTimeInMillis, alarm.getRepeatDays(), alarm.isRepeat()));

                        alarmCreator.setNewAlarm(ctx,alarmTimeInMillis,alarm.getId());
                        alarm.setStatus(true);

                    }else {
                        //Toast.makeText(ctx, "UnChecked", Toast.LENGTH_SHORT).show();

                        alarmCreator.setAlarmOff(ctx,alarm.getId());
                        alarm.setStatus(false);
                    }
                    task.savePersonalAlarms(ctx,alarms);
                    break;
            }

        }
    }
}
