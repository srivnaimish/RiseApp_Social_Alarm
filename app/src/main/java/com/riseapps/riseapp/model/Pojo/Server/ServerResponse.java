package com.riseapps.riseapp.model.Pojo.Server;

public class ServerResponse {

    private String result;
    private String message;

    public ServerResponse() {
    }

    public ServerResponse(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
