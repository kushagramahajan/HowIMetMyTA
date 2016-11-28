package com.example.kushagra.meetupapp.network.model;

/**
 * Created by kushagra on 29-10-2016.
 */

public class StatusClass
{

    boolean isAnyOld;
    boolean isAnyNew;

    String[] oldQueryId;
    String[] newQueryId;
    String[] newCourseIds;

    public String[] getNewCourseIds() {
        return newCourseIds;
    }

    public void setNewCourseIds(String[] newCourseIds) {
        this.newCourseIds = newCourseIds;
    }

    public String[] getOldCourseIds() {
        return oldCourseIds;
    }

    public void setOldCourseIds(String[] oldCourseIds) {
        this.oldCourseIds = oldCourseIds;
    }

    String[] oldCourseIds;

    String studentId;

    public StatusClass(boolean isAnyOld, boolean isAnyNew, String[] oldQueryId,String[] newQueryId) {
        this.isAnyOld = isAnyOld;
        this.isAnyNew = isAnyNew;
        this.oldQueryId = oldQueryId;
        this.newQueryId = newQueryId;
    }

    public StatusClass(String studentId) {

        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isAnyOld() {
        return isAnyOld;
    }

    public void setAnyOld(boolean anyOld) {
        isAnyOld = anyOld;
    }

    public boolean isAnyNew() {
        return isAnyNew;
    }

    public void setAnyNew(boolean anyNew) {
        isAnyNew = anyNew;
    }

    public String[] getOldQueryId() {
        return oldQueryId;
    }

    public void setOldQueryId(String[] oldQueryId) {
        this.oldQueryId = oldQueryId;
    }

    public String[] getNewQueryId() {
        return newQueryId;
    }

    public void setNewQueryId(String[] newQueryId) {
        this.newQueryId = newQueryId;
    }
}
