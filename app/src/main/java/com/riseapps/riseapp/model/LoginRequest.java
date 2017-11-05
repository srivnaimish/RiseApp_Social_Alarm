
package com.riseapps.riseapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    private String operation;
    private User user;

    public LoginRequest() {
    }

    public LoginRequest(String operation, User user) {
        super();
        this.operation = operation;
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
