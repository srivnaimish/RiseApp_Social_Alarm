package com.riseapps.riseapp.executor.Interface;

import com.riseapps.riseapp.model.Pojo.Contact;

import java.util.ArrayList;

/**
 * Created by naimish on 30/11/17.
 */

public interface ContactCallback {
    public void onSuccessfulFetch(ArrayList<Contact> contacts);
    public void onUnsuccessfulFetch();
}
