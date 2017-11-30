package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by naimish on 15/11/17.
 */
@Entity
public class Contact_Entity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "contact_initials")
    private String initials;

    @ColumnInfo(name = "contact_name")
    private String name;

    @ColumnInfo(name = "contact_number")
    private String number;

    @ColumnInfo(name = "contact_selection")
    private boolean selection;

    public Contact_Entity(String initials, String name, String number, boolean selection) {
        this.initials = initials;
        this.name = name;
        this.number = number;
        this.selection = selection;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
