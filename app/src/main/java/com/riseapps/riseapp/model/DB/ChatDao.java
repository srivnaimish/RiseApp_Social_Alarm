package com.riseapps.riseapp.model.DB;

import android.arch.lifecycle.LiveData;
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
    LiveData<List<Chat_Entity>> getChatMessages(String chat_id);

    @Insert
    void insertChat(Chat_Entity chat_entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSummary(ChatSummary chatSummary);

    @Query("DELETE FROM chat_entity WHERE chat_id=:chat_id")
    public void clearChat(String chat_id);

  /*  @Query("DELETE FROM ChatSummary WHERE chat_contact_number=:chat_contact_number")
    public void clearSummary(String chat_id);*/

    @Query("UPDATE chatsummary SET last_message = :value  WHERE chat_contact_number = :chat_contact_number")
    public void clearSummary(String chat_contact_number,String value);

    @Query("DELETE FROM ChatSummary WHERE chat_contact_number=:chat_contact_number")
    public void deleteSummary(String chat_contact_number);

    @Query("SELECT * FROM chatsummary")
    LiveData<List<ChatSummary>> getChatSummaries();

    @Query("UPDATE chatsummary SET read_messages = :value  WHERE chat_contact_number = :chat_contact_number")
    void updateReadStatus(String chat_contact_number,boolean value);

    @Query("SELECT * FROM chat_entity " +
            "WHERE read=:read AND chat_time<:currentTime ORDER BY chat_time DESC")
    List<Chat_Entity> getPendingReminders(boolean read,long currentTime);

    @Query("SELECT * FROM chat_entity " +
            "WHERE read=:read AND chat_time<=:endTime AND chat_time>=:startTime ORDER BY chat_time ASC")
    List<Chat_Entity> getTodaysReminders(boolean read,long startTime,long endTime);

    @Query("UPDATE chat_entity SET read = :value  WHERE message_id = :message_id")
    void updatePendingStatus(int message_id,boolean value);

}