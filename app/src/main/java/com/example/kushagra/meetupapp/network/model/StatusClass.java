package com.example.kushagra.meetupapp.network.model;

import java.util.ArrayList;

/**
 * Created by kushagra on 29-10-2016.
 */

public class StatusClass {
    boolean isAnyOld;
    boolean isAnyNew;

    String[] oldQueryId;
    String[] newQueryId;

    public StatusClass(boolean isAnyOld, boolean isAnyNew, String[] oldQueryId,String[] newQueryId) {
        this.isAnyOld = isAnyOld;
        this.isAnyNew = isAnyNew;
        this.oldQueryId = oldQueryId;
        this.newQueryId = newQueryId;
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
