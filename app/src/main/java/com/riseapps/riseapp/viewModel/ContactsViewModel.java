package com.riseapps.riseapp.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naimish on 13/12/17.
 */

public class ContactsViewModel extends ViewModel {

    private LiveData<List<Contact_Entity>> contactList;

    public LiveData<List<Contact_Entity>> getContactList(MyDB myDB) {
        contactList = myDB.contactDao().getAll();
        return contactList;
    }

    public ArrayList<Contact_Entity> getContacts() {
        return (ArrayList<Contact_Entity>) contactList.getValue();
    }

}