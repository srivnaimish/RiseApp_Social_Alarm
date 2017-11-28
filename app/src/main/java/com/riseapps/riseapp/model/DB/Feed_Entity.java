package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by naimish on 15/11/17.
 */
@Entity
public class Feed_Entity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "feed_type")
    private int type;

    @ColumnInfo(name = "feed_sender")
    private String message;

    @ColumnInfo(name = "feed_time")
    private long time;

    @ColumnInfo(name = "feed_note")
    private String note;

    @ColumnInfo(name = "feed_url")
    private String imageurl;

    public Feed_Entity() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
