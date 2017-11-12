package com.riseapps.riseapp.model.Pojo;

/**
 * Created by naimish on 12/11/17.
 */

public class ReceivedReminder {
    private String sender;
    private String time;
    private String note;
    private String image;

    public ReceivedReminder(String sender, String time, String note, String image) {
        this.sender = sender;
        this.time = time;
        this.note = note;
        this.image = image;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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
}
