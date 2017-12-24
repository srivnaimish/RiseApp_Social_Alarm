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

    @Insert     //Insert in Chat
    void insertChat(Chat_Entity chat_entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)    //Insert in Summary
    void insertSummary(ChatSummary chatSummary);

    @Query("SELECT * FROM chat_entity " +
            "WHERE chat_id=:chat_id")       //Get all messages from particular number
    LiveData<List<Chat_Entity>> getChatMessages(String chat_id);

    @Query("SELECT * FROM chatsummary")     //Get all summaries
    LiveData<List<ChatSummary>> getChatSummaries();

    @Query("SELECT * FROM chat_entity " +
            "WHERE read=:read AND chat_time<:currentTime ORDER BY chat_time DESC")      //Get all pending tasks
    List<Chat_Entity> getPendingReminders(boolean read,long currentTime);

    @Query("SELECT * FROM chat_entity " +
            "WHERE read=:read AND chat_time<=:endTime AND chat_time>=:startTime ORDER BY chat_time ASC")    //Get all today tasks
    List<Chat_Entity> getTodaysReminders(boolean read,long startTime,long endTime);

    @Query("DELETE FROM chat_entity WHERE chat_id=:chat_id")   //Clear All Chat Messages for a number
    public void clearChat(String chat_id);

    @Query("UPDATE chatsummary SET last_message = :value  WHERE chat_contact_number = :chat_contact_number")
    public void clearSummary(String chat_contact_number,String value);  //Clear last message in chat summary on clear all

    @Query("DELETE FROM ChatSummary WHERE chat_contact_number=:chat_contact_number")
    public void deleteSummary(String chat_contact_number);      //delete chat summary on long press

    @Query("UPDATE chatsummary SET read_messages = :value  WHERE chat_contact_number = :chat_contact_number")
    void updateReadStatus(String chat_contact_number,boolean value);    //update chat summary read status

    @Query("UPDATE chat_entity SET read = :value  WHERE message_id = :message_id")
    void updatePendingStatus(int message_id,boolean value);     //update task pending status

    @Query("DELETE FROM chat_entity WHERE message_id=:message_id")
    public void deleteChatTask(int message_id);     //delete a single message in a chat

}