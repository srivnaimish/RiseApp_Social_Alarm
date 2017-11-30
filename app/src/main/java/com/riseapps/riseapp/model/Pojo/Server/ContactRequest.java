package com.riseapps.riseapp.model.Pojo.Server;

import java.util.ArrayList;

public class ContactRequest {

    private String operation;
    private ArrayList<String> phones;

    public ContactRequest() {
    }

    public ContactRequest(String operation, ArrayList<String> phones) {
        super();
        this.operation = operation;
        this.phones = phones;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }
}
