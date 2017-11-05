
package com.riseapps.riseapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private String result;
    private String message;
    private String name;

    public LoginResponse() {
    }

    public LoginResponse(String result, String message, String name) {
        this.result = result;
        this.message = message;
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
