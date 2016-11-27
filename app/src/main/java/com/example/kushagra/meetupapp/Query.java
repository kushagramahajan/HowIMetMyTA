package com.example.kushagra.meetupapp;

import java.io.Serializable;

/**
 * Created by Prankul on 27-10-2016.
 */

public class Query implements Serializable {
    String title,query, taId;
    String queryId,studentId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Query(String qid, String title, String query, String taId) {
        this.title = title;
        this.query = query;
        this.taId = taId;
        this.queryId=qid;
    }

    public String getTaId() {
        return taId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setTaId(String taId) {
        this.taId = taId;
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

 }
