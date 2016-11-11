package com.example.kushagra.meetupapp;

import java.util.ArrayList;

/**
 * Created by Himanshu Sagar on 16-10-2016.
 */

public class Course
{
    private String courseName,courseId;
    private ArrayList<String> TaArray;


    public Course(String courseId, String courseName, ArrayList<String> taArray)
    {
        this.courseName = courseName;
        this.courseId = courseId;
        TaArray = taArray;
        TaArray.toArray();
    }

    public Course(String courseName) {
        this.courseName = courseName;
    }


    public String getCourseName() {

        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public ArrayList<String> getTaArray() {
        return TaArray;
    }

    public void setTaArray(ArrayList<String> taArray) {
        TaArray = taArray;
    }
}
