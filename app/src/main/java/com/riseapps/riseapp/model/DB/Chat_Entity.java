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
    private int id;
    @ColumnInfo(name = "chat_sender")
    private String sender;
    @ColumnInfo(name = "chat_reciver")
    private String reciever;
    @ColumnInfo(name = "chat_time")
    private String time;
    @ColumnInfo(name = "contact_note")
    private String note;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "read_status")
    private boolean read_status;

    public Chat_Entity(String sender, String reciever, String time, String note, String image, boolean read_status) {
        this.sender = sender;
        this.reciever = reciever;
        this.time = time;
        this.note = note;
        this.image = image;
        this.read_status = read_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
}
