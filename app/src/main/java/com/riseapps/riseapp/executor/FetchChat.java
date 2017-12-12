package com.riseapps.riseapp.executor;

import android.os.AsyncTask;
import android.util.Log;

import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.MyDB;

import java.util.ArrayList;

/**
 * Created by naimish on 29/11/17.
 */

public class FetchChat extends AsyncTask<Void, Void, ArrayList<Chat_Entity>> {

    private ChatCallback chatCallback;
    private MyDB myDB;
    private String chat_id;


    public FetchChat(String chat_id,MyDB myDB, ChatCallback chatCallback) {     //for choice GET_CHAT_SUMMARIES
        this.chat_id=chat_id;
        this.myDB=myDB;
        this.chatCallback=chatCallback;
    }

    @Override
    protected ArrayList<Chat_Entity> doInBackground(Void... voids) {
        ArrayList<Chat_Entity> chat_entities = (ArrayList<Chat_Entity>) myDB.chatDao().getChatMessages(chat_id);
        Log.d("Chat fetch","Successful "+ chat_entities.size());
        myDB.chatDao().updateReadStatus(chat_id,true);
        return chat_entities;
    }

    @Override
    protected void onPostExecute(ArrayList<Chat_Entity> chat_entities) {
        super.onPostExecute(chat_entities);
        chatCallback.chatFetched(chat_entities);
    }
}