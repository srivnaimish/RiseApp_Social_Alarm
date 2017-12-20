package com.riseapps.riseapp.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.MyDB;

import java.util.Calendar;
import java.util.List;

/**
 * Created by naimish on 13/12/17.
 */

public class PendingViewModel extends ViewModel {

    private LiveData<List<Chat_Entity>> reminderList;

    public LiveData<List<Chat_Entity>> getPendingList(MyDB myDB) {
        reminderList = myDB.chatDao().getPendingReminders(false,System.currentTimeMillis());
        return reminderList;
    }

    public LiveData<List<Chat_Entity>> getTodayList(MyDB myDB) {

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        long startTime=calendar.getTimeInMillis();
        long endTime=startTime+86400000;

       // currentTime=currentTime-((hour*60*60*1000)+(minute*60*1000)+(sec*1000));
        reminderList = myDB.chatDao().getTodaysReminders(false,startTime,endTime);
        return reminderList;
    }

}