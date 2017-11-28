package com.riseapps.riseapp.model.Pojo;

/**
 * Created by naimish on 25/11/17.
 */

public class Contact {
    private String initials;
    private String name;
    private String number;
    private boolean selected;

    public Contact(String initials, String name, String number, boolean selected) {
        this.initials = initials;
        this.name = name;
        this.number = number;
        this.selected = selected;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
