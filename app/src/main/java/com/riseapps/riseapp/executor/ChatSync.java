package com.riseapps.riseapp.executor;

import android.os.AsyncTask;
import android.util.Log;

import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.DB.ChatSummary;

import java.util.ArrayList;

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


    public ChatSync(MyDB myDB,int choice) {     //for choice GET_CHAT_SUMMARIES
        this.myDB=myDB;
        this.choice=choice;
    }

    public ChatSync(int contact_id,MyDB myDB,int choice) {   // for choice GET_CHAT
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
            case GET_CHAT_SUMMARIES:
                getChatSummaries();
                break;
            case GET_CHAT:
                getChatWithContact();
                break;
            case INSERT_NEW_CHAT:
                insertChatMessage();
                break;
        }

        return null;
    }

    private void getChatSummaries(){
        ArrayList<ChatSummary> chatSummaries=(ArrayList<ChatSummary>) myDB.chatDao().getChatSummaries();
        chatCallback.summariesFetched(chatSummaries);
        Log.d("Chat Summaries","Successful "+chatSummaries.size());
    }

    private void getChatWithContact() {
        ArrayList<Chat_Entity> chat_entities=(ArrayList<Chat_Entity>) myDB.chatDao().getChatMessages(contact_id);
        chatCallback.chatFetched(chat_entities);
        Log.d("Chat Fetched","Successful "+chat_entities.size());
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
            chat_entity.setRead_status(read);
            myDB.chatDao().insertChat(chat_entity);

            ChatSummary chatSummary=new ChatSummary();
            chatSummary.setChat_contact_id(contact.getId());
            chatSummary.setChat_contact_name(contact.getName());
            chatSummary.setRead(read);
            myDB.chatDao().insertSummary(chatSummary);
        }

        Log.d("Insert","Successful");
    }


    public void setChatCallback(ChatCallback chatCallback) {
        this.chatCallback = chatCallback;
    }
}