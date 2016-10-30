package com.example.kushagra.meetupapp.network.model;

import com.example.kushagra.meetupapp.db.objects.Course;

import java.util.ArrayList;

/**
 * Created by kushagra on 15-10-2016.
 */
public class StudentRegisterClass
{
    private String studentId;
    private String name;
    private ArrayList<Course> courses;

    public StudentRegisterClass(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
