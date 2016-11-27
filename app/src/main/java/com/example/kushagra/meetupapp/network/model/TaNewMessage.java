package com.example.kushagra.meetupapp.network.model;

/**
 * Created by kushagra on 07-11-2016.
 */

public class TaNewMessage {

    public String taId,studentId,courseId,title,description;
    public boolean isResolved;
    public String queryId;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public TaNewMessage(String queryId) {
        this.queryId = queryId;
    }

    public TaNewMessage(String taId, String studentId, String courseId, String title, String description, boolean isResolved, String queryId) {
        this.taId = taId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.isResolved = isResolved;
        this.queryId = queryId;
    }

    public String getTaId() {
        return taId;
    }

    public void setTaId(String taId) {
        this.taId = taId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
}
