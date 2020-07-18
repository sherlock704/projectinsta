package com.example.projectpro.model;

public class Chat {
    String receiver;
    String sender;
    String message;

    public Chat(String receiver, String sender, String message) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
    }

    public Chat() {
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
