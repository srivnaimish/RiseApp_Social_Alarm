package com.riseapps.riseapp.executor.Interface;

import com.riseapps.riseapp.model.DB.Chat_Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naimish on 15/12/17.
 */

public interface RemindersCallback {
    public void onRemindersFetch(List<Chat_Entity> reminderList);
}
