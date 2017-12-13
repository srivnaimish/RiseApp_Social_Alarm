package com.riseapps.riseapp.executor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.MyDB;

import java.util.List;

/**
 * Created by naimish on 13/12/17.
 */

public class SummaryViewModel extends ViewModel {

    private LiveData<List<ChatSummary>> summaryList;

    public LiveData<List<ChatSummary>> getSummaryList(MyDB myDB) {
        summaryList = myDB.chatDao().getChatSummaries();
        return summaryList;
    }

}