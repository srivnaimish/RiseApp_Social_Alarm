package com.riseapps.riseapp.model.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by naimish on 15/11/17.
 */
@Entity
public class Contact_Entity {

    @NonNull @PrimaryKey @ColumnInfo(name = "contact_number")
    private String number;

    @ColumnInfo(name = "contact_name")
    private String name;

    @ColumnInfo(name = "contact_selection")
    private boolean selection;

    public Contact_Entity( String name, @NonNull String number, boolean selection) {
        this.name = name;
        this.number = number;
        this.selection = selection;
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

}
