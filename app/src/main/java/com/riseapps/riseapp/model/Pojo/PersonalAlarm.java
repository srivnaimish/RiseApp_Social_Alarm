package com.riseapps.riseapp.model.Pojo;

import java.util.Calendar;

/**
 * Created by naimish on 31/10/17.
 */

public class PersonalAlarm {
    private int id;
    private Calendar calendar;
    private boolean status;
    private boolean repeat;
    private boolean repeatDays[];
    private long alarmTimeInMillis;
    private String tone;
    private boolean vibrate;

    public PersonalAlarm(int id, Calendar calendar, boolean status, boolean repeat, boolean[] repeatDays, long alarmTimeInMillis, String tone, boolean vibrate) {
        this.id = id;
        this.calendar = calendar;
        this.status = status;
        this.repeat = repeat;
        this.repeatDays = repeatDays;
        this.alarmTimeInMillis = alarmTimeInMillis;
        this.tone = tone;
        this.vibrate = vibrate;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean[] getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(boolean[] repeatDays) {
        this.repeatDays = repeatDays;
    }

    public long getAlarmTimeInMillis() {
        return alarmTimeInMillis;
    }

    public void setAlarmTimeInMillis(long alarmTimeInMillis) {
        this.alarmTimeInMillis = alarmTimeInMillis;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
