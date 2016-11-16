package com.example.kushagra.meetupapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Prankul on 27-10-2016.
 */

public class Query implements Serializable {
    String title,query,receiver;
    String queryId;
    ArrayList<Message> messages;

    public Query(String qid, String title, String query, String receiver,ArrayList<Message> messages) {
        this.title = title;
        this.query = query;
        this.receiver=receiver;
        this.messages = messages;
        this.queryId=qid;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
