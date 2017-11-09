
package com.riseapps.riseapp.model;

public class User {

    private String UID;
    private String name;
    private String email;

    public User() {
    }

    public User(String UID, String name, String email) {
        super();
        this.UID = UID;
        this.name = name;
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPhone() {
        return email;
    }

    public void setPhone(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
