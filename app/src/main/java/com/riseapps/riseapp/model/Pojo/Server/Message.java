package com.riseapps.riseapp.model.Pojo.Server;

import java.util.ArrayList;

/**
 * Created by naimish on 9/11/17.
 */

public class Message {
    private String Sender;
    private ArrayList<String> Recipients;
    private long Time;
    private String Note;

    public Message(String Sender, ArrayList<String> Recipients, long Time, String Note) {
        this.Sender = Sender;
        this.Recipients = Recipients;
        this.Time = Time;
        this.Note = Note;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public ArrayList<String> getRecipients() {
        return Recipients;
    }

    public void setRecipients(ArrayList<String> recipients) {
        Recipients = recipients;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long Time) {
        this.Time = Time;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

}
