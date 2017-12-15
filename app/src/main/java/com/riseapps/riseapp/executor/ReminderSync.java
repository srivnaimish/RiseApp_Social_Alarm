package com.riseapps.riseapp.executor;

import android.content.Context;
import android.os.AsyncTask;

import com.riseapps.riseapp.executor.Interface.RemindersCallback;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.MyDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naimish on 29/11/17.
 */

public class ReminderSync extends AsyncTask<Void, Void, List<Chat_Entity>> {

    private RemindersCallback remindersCallback;
    private MyDB myDB;
    private ArrayList<Chat_Entity> reminders;

    public ReminderSync(Context context,MyDB myDB) {
        this.myDB=myDB;
        remindersCallback= (RemindersCallback) context;
    }

    @Override
    protected List<Chat_Entity> doInBackground(Void... voids) {
        return myDB.chatDao().getReminders(false,System.currentTimeMillis());
    }

    @Override
    protected void onPostExecute(List<Chat_Entity> chat_entities) {
        super.onPostExecute(chat_entities);
        remindersCallback.onRemindersFetch(chat_entities);
    }

}