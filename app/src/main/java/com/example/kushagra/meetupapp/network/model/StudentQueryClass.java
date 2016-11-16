package com.example.kushagra.meetupapp.network.model;

/**
 * Created by Himanshu Sagar on 21-10-2016.
 */

public class StudentQueryClass
{
    String queryId;
    private String title,description,studentId , courseId;
    boolean isGeneric;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getTaId() {
        return taId;
    }

    public void setTaId(String taId) {
        this.taId = taId;
    }

    private String taId;

    public StudentQueryClass(String title, String description, String studentId, String courseId, boolean isGeneric ,String taId) {
        this.title = title;
        this.description = description;
        this.studentId = studentId;
        this.courseId = courseId;
        this.isGeneric = isGeneric;
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

    public boolean isGeneric() {
        return isGeneric;
    }

    public void setGeneric(boolean generic) {
        isGeneric = generic;
    }
}
