package com.riseapps.riseapp.model.Pojo.Server;

import java.util.ArrayList;

/**
 * Created by naimish on 29/11/17.
 */

public class ContactsResponse {
    private boolean[] result;
    private String message;

    public ContactsResponse(boolean[] result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean[] getResult() {
        return result;
    }

    public void setResult(boolean[] result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
