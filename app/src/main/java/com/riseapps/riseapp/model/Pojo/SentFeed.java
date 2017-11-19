package com.riseapps.riseapp.model.Pojo;

/**
 * Created by naimish on 12/11/17.
 */

public class SentFeed {
    private String people;
    private String time;
    private String note;
    private String image;

    public SentFeed(String people, String time, String note, String image) {
        this.people = people;
        this.time = time;
        this.note = note;
        this.image = image;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
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
