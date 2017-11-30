package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by naimish on 15/11/17.
 */

@Database(entities = {Contact_Entity.class,Chat_Entity.class}, version = 2)
public abstract class MyDB extends RoomDatabase {
    public abstract ContactDao contactDao();
}
