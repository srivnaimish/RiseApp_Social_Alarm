package com.riseapps.riseapp.executor;

import android.os.AsyncTask;
import android.util.Log;

import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;

import java.util.ArrayList;

import static com.riseapps.riseapp.Components.AppConstants.DELETE_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;

/**
 * Created by naimish on 29/11/17.
 */

public class FetchChatSummaries extends AsyncTask<Void, Void, ArrayList<ChatSummary>> {

    private ChatCallback chatCallback;
    private MyDB myDB;


    public FetchChatSummaries(MyDB myDB,ChatCallback chatCallback) {     //for choice GET_CHAT_SUMMARIES
        this.myDB=myDB;
        this.chatCallback=chatCallback;
    }

    @Override
    protected ArrayList<ChatSummary> doInBackground(Void... voids) {
        ArrayList<ChatSummary> chatSummaries = (ArrayList<ChatSummary>) myDB.chatDao().getChatSummaries();
        Log.d("Chat Summaries","Successful "+ chatSummaries.size());
        return chatSummaries;
    }

    @Override
    protected void onPostExecute(ArrayList<ChatSummary> chatSummaries) {
        super.onPostExecute(chatSummaries);
        chatCallback.summariesFetched(chatSummaries);
    }
}