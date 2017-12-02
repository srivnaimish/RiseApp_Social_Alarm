package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by naimish on 30/11/17.
 */
@Entity
public class Chat_Entity {
    @PrimaryKey(autoGenerate = true)
    private int message_id;

    @ColumnInfo(name = "chat_contact_id")
    private int contact_id;
    @ColumnInfo(name = "chat_contact_name")
    private String contact_name;
    @ColumnInfo(name = "chat_time")
    private long time;
    @ColumnInfo(name = "chat_note")
    private String note;
    @ColumnInfo(name = "chat_image")
    private String image;
    @ColumnInfo(name = "sent_or_recieved")
    private int sent_or_recieved;
    @ColumnInfo(name = "chat_read_status")
    private boolean read_status;

    public Chat_Entity() {

    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isRead_status() {
        return read_status;
    }

    public void setRead_status(boolean read_status) {
        this.read_status = read_status;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getSent_or_recieved() {
        return sent_or_recieved;
    }

    public void setSent_or_recieved(int sent_or_recieved) {
        this.sent_or_recieved = sent_or_recieved;
    }
}
