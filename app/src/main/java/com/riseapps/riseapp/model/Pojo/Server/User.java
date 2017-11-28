package com.riseapps.riseapp.model.Pojo.Server;

public class User {

    private String UID;
    private String Name;
    private String Phone;

    public User() {
    }

    public User(String UID, String Name,String Phone) {
        super();
        this.UID = UID;
        this.Name = Name;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
