package com.riseapps.riseapp.model;

import android.arch.persistence.room.Room;
import android.support.multidex.MultiDexApplication;

import com.riseapps.riseapp.model.DB.MyDB;

/**
 * Created by naimish on 15/11/17.
 */

public class MyApplication extends MultiDexApplication {
    private MyDB database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), MyDB.class, "MyDB").build();
    }

    public MyDB getDatabase() {
        return database;
    }
}
