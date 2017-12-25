package com.riseapps.riseapp.model;

import android.arch.persistence.room.Room;
import android.support.multidex.MultiDexApplication;

import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.model.DB.MyDB;

/**
 * Created by naimish on 15/11/17.
 */

public class MyApplication extends MultiDexApplication {
    private MyDB database;
    SharedPreferenceSingelton sharedPreferenceSingelton=new SharedPreferenceSingelton();
    private String UID;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), MyDB.class, "MyDB").build();
    }

    public MyDB getDatabase() {
        return database;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

}
