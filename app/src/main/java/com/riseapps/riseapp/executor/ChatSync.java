package com.riseapps.riseapp.executor;

import android.os.AsyncTask;
import android.util.Log;

import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.DB.ChatSummary;

import java.util.ArrayList;

import static com.riseapps.riseapp.Components.AppConstants.CLEAR_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.DELETE_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.UPDATE_PENDING;
import static com.riseapps.riseapp.Components.AppConstants.UPDATE_SUMMARY;

/**
 * Created by naimish on 29/11/17.
 */

public class ChatSync extends AsyncTask<Void, Void, Void> {

    private MyDB myDB;
    private int choice;
    private String chat_id;

    private long timeInMillis;
    private String note;
    private String image;
    private int sent_or_recieved;
    private boolean read;

    private int message_id;

    private ArrayList<Contact_Entity> new_contacts;

    public ChatSync(String chat_id,MyDB myDB,int choice) {
        this.myDB=myDB;
        this.choice=choice;
        this.chat_id=chat_id;
    }

    public ChatSync(ArrayList<Contact_Entity> newContacts,long timeInMillis, String s, String s1, int i, boolean b, MyDB myDB, int choice) {   // for choice INSERT_NEW_CHAT
        this.myDB=myDB;
        this.choice=choice;
        this.new_contacts=newContacts;
        this.timeInMillis=timeInMillis;
        this.note=s;
        this.image=s1;
        this.sent_or_recieved=i;
        this.read=b;
    }

    public ChatSync(int message_id,MyDB myDB,int choice) {
        this.myDB=myDB;
        this.choice=choice;
        this.message_id=message_id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        switch (choice){

            case INSERT_NEW_CHAT:
                insertChatMessage();
                break;

            case DELETE_CHAT:
                deleteChatAndSummary();
                break;

            case CLEAR_CHAT:
                clearChat();
                break;

            case UPDATE_SUMMARY:
                updateSummary();
                break;

            case UPDATE_PENDING:
                break;
        }

        return null;
    }

    private void insertChatMessage(){
        for (Contact_Entity contact:new_contacts) {
            Chat_Entity chat_entity = new Chat_Entity();
            chat_entity.setMessage_id((int) System.currentTimeMillis());
            chat_entity.setChat_id(contact.getNumber());
            chat_entity.setContact_name(contact.getName());
            chat_entity.setTime(timeInMillis);
            chat_entity.setNote(note);
            chat_entity.setImage(image);
            chat_entity.setSent_or_recieved(sent_or_recieved);
            chat_entity.setRead(true);
            myDB.chatDao().insertChat(chat_entity);

            ChatSummary chatSummary=new ChatSummary();
            chatSummary.setChat_contact_number(contact.getNumber());
            chatSummary.setChat_contact_name(contact.getName());
            chatSummary.setRead(read);
            chatSummary.setChat_last_message(note);

            myDB.chatDao().insertSummary(chatSummary);
        }
        Log.d("Insert","Successful");
    }

    private void deleteChatAndSummary(){
        myDB.chatDao().deleteChat(chat_id);
        myDB.chatDao().deleteSummary(chat_id);
        Log.d("Delete","Successful");
    }

    private void clearChat(){
        myDB.chatDao().deleteChat(chat_id);
    }

    private void updateSummary() {
        myDB.chatDao().updateReadStatus(chat_id,true);
    }

    private void updatePending() {
        myDB.chatDao().updatePendingStatus(message_id,true);
    }


}