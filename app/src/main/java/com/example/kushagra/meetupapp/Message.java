package com.example.kushagra.meetupapp;

/**
 * Created by Prankul on 27-10-2016.
 */

public class Message {
    String sender, receiver, message;

    public Message(String from, String to, String messege) {
        this.sender = from;
        this.receiver = to;
        this.message = messege;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
