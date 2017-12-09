package com.riseapps.riseapp.executor;

import android.os.AsyncTask;
import android.util.Log;

import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.DB.ChatSummary;

import java.util.ArrayList;

import static com.riseapps.riseapp.Components.AppConstants.DELETE_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.GET_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.GET_CHAT_SUMMARIES;
import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;

/**
 * Created by naimish on 29/11/17.
 */

public class ChatSync extends AsyncTask<Void, Void, Void> {

    private ChatCallback chatCallback;
    private MyDB myDB;
    private int choice;
    private int contact_id;

    private long timeInMillis;
    private String note;
    private String image;
    private int sent_or_recieved;
    private boolean read;

    private ArrayList<Contact_Entity> new_contacts;

    public ChatSync(int contact_id,MyDB myDB,int choice) {   // for choice  & DELETE_CHAT
        this.myDB=myDB;
        this.choice=choice;
        this.contact_id=contact_id;
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

    @Override
    protected Void doInBackground(Void... voids) {
        switch (choice){

            case INSERT_NEW_CHAT:
                insertChatMessage();
                break;
            case DELETE_CHAT:
                deleteChat();
                break;
        }

        return null;
    }

    private void insertChatMessage(){
        for (Contact_Entity contact:new_contacts) {
            Chat_Entity chat_entity = new Chat_Entity();
            chat_entity.setContact_id(contact.getId());
            chat_entity.setContact_name(contact.getName());
            chat_entity.setTime(timeInMillis);
            chat_entity.setNote(note);
            chat_entity.setImage(image);
            chat_entity.setSent_or_recieved(sent_or_recieved);
            myDB.chatDao().insertChat(chat_entity);

            ChatSummary chatSummary=new ChatSummary();
            chatSummary.setChat_contact_id(contact.getId());
            chatSummary.setChat_contact_name(contact.getName());
            chatSummary.setChat_contact_number(contact.getNumber());
            chatSummary.setRead(read);
            myDB.chatDao().insertSummary(chatSummary);

        }

        Log.d("Insert","Successful");
    }

    private void deleteChat(){
        myDB.chatDao().deleteChat(contact_id);
        myDB.chatDao().deleteSummary(contact_id);
        Log.d("Delete","Successful");
    }

    public void setChatCallback(ChatCallback chatCallback) {
        this.chatCallback = chatCallback;
    }
}