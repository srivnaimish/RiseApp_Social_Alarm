package com.riseapps.riseapp.executor.Interface;

import com.riseapps.riseapp.model.DB.Contact_Entity;

import java.util.ArrayList;

/**
 * Created by naimish on 30/11/17.
 */

public interface ContactCallback {
    public void onSuccessfulFetch(ArrayList<Contact_Entity> contacts,boolean restart_async);
    public void onUnsuccessfulFetch();
}
