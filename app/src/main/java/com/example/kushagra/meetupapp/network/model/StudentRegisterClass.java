package com.example.kushagra.meetupapp.network.model;

/**
 * Created by kushagra on 15-10-2016.
 */
public class StudentRegisterClass
{
    private String studentId;
    private String name;
    private String[] myStudentCourses;
    private String[] myTaCourses;

    public String[] getMyStudentCourses() {
        return myStudentCourses;
    }

    public void setMyStudentCourses(String[] myStudentCourses) {
        this.myStudentCourses = myStudentCourses;
    }

    public String[] getMyTaCourses() {
        return myTaCourses;
    }

    public void setMyTaCourses(String[] myTaCourses) {
        this.myTaCourses = myTaCourses;
    }

    public StudentRegisterClass(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
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
