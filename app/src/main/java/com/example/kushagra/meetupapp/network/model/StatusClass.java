package com.example.kushagra.meetupapp.network.model;

import java.util.ArrayList;

/**
 * Created by kushagra on 29-10-2016.
 */

public class StatusClass {
    boolean isAnyOld;
    boolean isAnyNew;

    ArrayList<String> oldQueryId;
    ArrayList<String> newQueryId;

    public StatusClass(boolean isAnyOld, boolean isAnyNew, ArrayList<String> oldQueryId, ArrayList<String> newQueryId) {
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

    public ArrayList<String> getOldQueryId() {
        return oldQueryId;
    }

    public void setOldQueryId(ArrayList<String> oldQueryId) {
        this.oldQueryId = oldQueryId;
    }

    public ArrayList<String> getNewQueryId() {
        return newQueryId;
    }

    public void setNewQueryId(ArrayList<String> newQueryId) {
        this.newQueryId = newQueryId;
    }
}
