package com.riseapps.riseapp.executor.Interface;

import com.riseapps.riseapp.model.DB.Chat_Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naimish on 15/12/17.
 */

public interface RemindersCallback {
    public void onPendingFetch(ArrayList<Chat_Entity> pendingList);
    public void onTodaysFetch(ArrayList<Chat_Entity> todaysList);
}
