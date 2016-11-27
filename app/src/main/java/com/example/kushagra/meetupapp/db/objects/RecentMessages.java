package com.example.kushagra.meetupapp.db.objects;

import com.example.kushagra.meetupapp.Message;

/**
 * Created by kushagra on 27-11-2016.
 */

public class RecentMessages {

    String queryId;
    Message[] messages;

    public RecentMessages(String queryId, Message[] message) {
        this.queryId = queryId;
        this.messages=message;

    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }
}
