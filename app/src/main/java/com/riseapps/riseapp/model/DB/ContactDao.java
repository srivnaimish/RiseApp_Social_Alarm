package com.riseapps.riseapp.model.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.riseapps.riseapp.model.Pojo.ContactFetch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naimish on 15/11/17.
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact_entity")
    LiveData<List<Contact_Entity>> getAll();

    @Insert
    void insertFeed(List<Contact_Entity> contact_entities);

    @Query("DELETE FROM contact_entity")
    public void clearContacts();

    @Query("SELECT COUNT(*) FROM contact_entity WHERE contact_number=:phone")
    int isContactPresent(String phone);

    @Query("SELECT contact_name,contact_number FROM contact_entity WHERE contact_number=:phone")
    ContactFetch getContact(String phone);

}