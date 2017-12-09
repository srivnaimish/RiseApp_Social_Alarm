package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by naimish on 2/12/17.
 */
@Entity
public class ChatSummary {
    @PrimaryKey
    private int chat_contact_id;
    @ColumnInfo(name = "chat_contact_name")
    private String chat_contact_name;
    @ColumnInfo(name = "chat_contact_number")
    private String chat_contact_number;
    @ColumnInfo(name = "read_messages")
    private boolean read;

    public ChatSummary(){}

    public int getChat_contact_id() {
        return chat_contact_id;
    }

    public void setChat_contact_id(int chat_contact_id) {
        this.chat_contact_id = chat_contact_id;
    }

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
}
