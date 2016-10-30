package com.example.kushagra.meetupapp.db.objects;

/**
 * Created by Himanshu Sagar on 20-10-2016.
 */
public class ScheduleSlot
{
    private String type;
    private String timeEnd;
    private String timeBegin;
    private String day;

    public ScheduleSlot(String type, String day, String timeBegin, String timeEnd)
    {
        this.type = type;
        this.timeEnd = timeEnd;
        this.timeBegin = timeBegin;
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
