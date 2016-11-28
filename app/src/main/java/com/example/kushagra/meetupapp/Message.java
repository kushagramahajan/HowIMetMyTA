package com.example.kushagra.meetupapp;

/**
 * Created by Prankul on 27-10-2016.
 */

public class Message {
    String sender, receiver, message ,queryId;

    public Message(String from, String to, String messege , String queryId) {
        this.sender = from;
        this.receiver = to;
        this.message = messege;
        this.queryId = queryId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
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
