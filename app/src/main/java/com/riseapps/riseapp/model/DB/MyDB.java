package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by naimish on 15/11/17.
 */

@Database(entities = {Contact_Entity.class,Chat_Entity.class, ChatSummary.class}, version = 2)
public abstract class MyDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract ChatDao chatDao();
}
