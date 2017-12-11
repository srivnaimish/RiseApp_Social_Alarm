package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by naimish on 15/11/17.
 */

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat_entity " +
            "WHERE chat_id=:chat_id")
    List<Chat_Entity> getChatMessages(String chat_id);

    @Insert
    void insertChat(Chat_Entity chat_entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSummary(ChatSummary chatSummary);

    @Query("DELETE FROM chat_entity WHERE chat_id=:chat_id")
    public void deleteChat(String chat_id);

    @Query("DELETE FROM ChatSummary WHERE chat_contact_number=:chat_contact_number")
    public void deleteSummary(String chat_contact_number);

    @Query("SELECT * FROM chatsummary")
    List<ChatSummary> getChatSummaries();

    @Query("UPDATE chatsummary SET read_messages = :value  WHERE chat_contact_number = :chat_contact_number")
    void updateReadStatus(String chat_contact_number,boolean value);
}