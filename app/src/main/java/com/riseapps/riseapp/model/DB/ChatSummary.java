package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by naimish on 2/12/17.
 */
@Entity
public class ChatSummary {

    @NonNull @PrimaryKey @ColumnInfo(name = "chat_contact_number")
    private String chat_contact_number;

    @ColumnInfo(name = "chat_contact_name")
    private String chat_contact_name;

    @ColumnInfo(name = "read_messages")
    private boolean read;

    @ColumnInfo(name = "last_message")
    private String chat_last_message;

    public ChatSummary(){}

    public String getChat_contact_name() {
        return chat_contact_name;
    }

    public void setChat_contact_name(String chat_contact_name) {
        this.chat_contact_name = chat_contact_name;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getChat_contact_number() {
        return chat_contact_number;
    }

    public void setChat_contact_number(String chat_contact_number) {
        this.chat_contact_number = chat_contact_number;
    }

    public String getChat_last_message() {
        return chat_last_message;
    }

    public void setChat_last_message(String chat_last_message) {
        this.chat_last_message = chat_last_message;
    }
}
