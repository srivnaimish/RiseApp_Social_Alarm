
package com.riseapps.riseapp.model.Pojo;

public class MessageRequest {

    private String operation;
    private Message message;

    public MessageRequest() {
    }

    public MessageRequest(String operation, Message message) {
        super();
        this.operation = operation;
        this.message = message;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
