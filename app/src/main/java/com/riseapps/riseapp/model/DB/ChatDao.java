package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by naimish on 15/11/17.
 */

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat_entity")
    List<Chat_Entity> getAll();

    @Insert
    void insertChat(Chat_Entity chat_entity);

    @Delete
    void deleteChat(Chat_Entity chat_entity);

}