package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naimish on 15/11/17.
 */

@Dao
public interface FeedDao {

    @Query("SELECT * FROM feed_entity")
    List<Feed_Entity> getAll();

    @Insert
    void insertFeed(Feed_Entity feed_entity);

}