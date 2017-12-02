package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by naimish on 15/11/17.
 */

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat_entity " +
            "WHERE chat_contact_id=:contact_id")
    List<Chat_Entity> getChatMessages(int contact_id);

    @Insert
    void insertChat(Chat_Entity chat_entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSummary(ChatSummary chatSummary);

    @Delete
    void deleteChat(Chat_Entity chat_entity);

    @Delete
    void deleteSummary(ChatSummary chatSummary);

    @Query("SELECT * FROM chatsummary")
    List<ChatSummary> getChatSummaries();

}