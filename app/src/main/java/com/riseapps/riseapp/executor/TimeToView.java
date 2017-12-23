package com.riseapps.riseapp.executor;

import com.riseapps.riseapp.Components.AppConstants;

import java.util.Calendar;

/**
 * Created by naimish on 31/10/17.
 */

public class TimeToView {

    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();

    public String getTimeAsText(int hour, int minutes) {
        String time = "";
        if (hour < 10) {
            if (minutes < 10)
                time += "0" + hour + ":0" + minutes;
            else
                time += "0" + hour + ":" + minutes;
        } else {
            if (minutes < 10)
                time += hour + ":0" + minutes;
            else
                time += hour + ":" + minutes;
        }
        return time;
    }

    public int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /*public String getNextTriggerDay(long milliseconds, boolean[] repeatDays, boolean repeat) {
        StringBuilder day = new StringBuilder();
        if (repeat) {
            for (int i = 0; i < 7; i++) {
                if (repeatDays[i]) {
                    day.append(AppConstants.weekdays[i]).append(" ");
                }
            }
        } else {
            long currentTime = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTime);
            int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(milliseconds);
            int alarmDate = calendar1.get(Calendar.DAY_OF_MONTH);
            if (currentDate == alarmDate)
                return "Today";
            else
                return "Tomorrow";
        }
        return day.toString();
    }*/
}
