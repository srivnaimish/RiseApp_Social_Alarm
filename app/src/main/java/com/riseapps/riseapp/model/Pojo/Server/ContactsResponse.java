package com.riseapps.riseapp.model.Pojo.Server;

import java.util.ArrayList;

/**
 * Created by naimish on 29/11/17.
 */

public class ContactsResponse {
    private String[] result;
    private String message;

    public ContactsResponse(String[] result, String message) {
        this.result = result;
        this.message = message;
    }

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
