package com.riseapps.riseapp.model.Pojo.Server;

public class User {

    private String UID;
    private String Phone;

    public User() {
    }

    public User(String UID, String Phone) {
        super();
        this.UID = UID;
        this.Phone = Phone;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
}
