package com.example.kushagra.meetupapp;

/**
 * Created by Prankul on 27-10-2016.
 */

public class Messege {
    String from,to,messege;

    public Messege(String from, String to, String messege) {
        this.from = from;
        this.to = to;
        this.messege = messege;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }
}
