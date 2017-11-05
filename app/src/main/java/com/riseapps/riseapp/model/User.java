
package com.riseapps.riseapp.model;

public class User {

    private String UID;
    private String phone;

    public User() {
    }

    public User(String UID, String phone) {
        super();
        this.UID = UID;
        this.phone = phone;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
