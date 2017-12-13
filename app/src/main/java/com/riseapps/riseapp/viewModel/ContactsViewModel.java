package com.riseapps.riseapp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.MyApplication;

import java.util.List;

/**
 * Created by naimish on 13/12/17.
 */

public class ChatViewModel extends ViewModel {

    private LiveData<List<Chat_Entity>> chatList;

    public LiveData<List<Chat_Entity>> getChatList(MyDB myDB,String chat_id) {
        chatList = myDB.chatDao().getChatMessages(chat_id);
        return chatList;
    }

}