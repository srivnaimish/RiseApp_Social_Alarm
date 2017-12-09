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
    private int contact_id;


    public FetchChat(int contact_id,MyDB myDB, ChatCallback chatCallback) {     //for choice GET_CHAT_SUMMARIES
        this.contact_id=contact_id;
        this.myDB=myDB;
        this.chatCallback=chatCallback;
    }

    @Override
    protected ArrayList<Chat_Entity> doInBackground(Void... voids) {
        ArrayList<Chat_Entity> chat_entities = (ArrayList<Chat_Entity>) myDB.chatDao().getChatMessages(contact_id);
        Log.d("Chat fetch","Successful "+ chat_entities.size());
        return chat_entities;
    }

    @Override
    protected void onPostExecute(ArrayList<Chat_Entity> chat_entities) {
        super.onPostExecute(chat_entities);
        chatCallback.chatFetched(chat_entities);
    }
}