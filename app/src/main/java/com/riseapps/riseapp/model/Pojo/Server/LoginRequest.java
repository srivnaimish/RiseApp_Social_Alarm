package com.riseapps.riseapp.model.Pojo.Server;

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
