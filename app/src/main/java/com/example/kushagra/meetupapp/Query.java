package com.example.kushagra.meetupapp;

import java.io.Serializable;

/**
 * Created by Prankul on 27-10-2016.
 */



public class Query implements Serializable
{
    String title, description, taId;
    String queryId,studentId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Query(String qid, String title, String description, String taId, String studentId) {
        this.title = title;
        this.description = description;
        this.taId = taId;
        this.queryId=qid;
        this.studentId=studentId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

 }
