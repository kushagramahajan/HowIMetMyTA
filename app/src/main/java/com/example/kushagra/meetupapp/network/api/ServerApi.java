package com.example.kushagra.meetupapp.network.api;

import com.example.kushagra.meetupapp.Messege;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.network.model.StatusClass;
import com.example.kushagra.meetupapp.network.model.StudentQueryClass;
import com.example.kushagra.meetupapp.network.model.StudentRegisterClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by kushagra on 15-10-2016.
 */
public interface ServerApi
{
    @POST("app/student/register")
    Call< StudentRegisterClass > registerStudentOnServer(@Body StudentRegisterClass studentRegisterObject);

    @GET("app/student/allCoursesList")
    Call<ArrayList<String> > getAllCoursesList();

    @POST("app/student/specificCourseData")
    Call<Course> getSpecificCourseData(@Body Course courseDataObject);

    @POST("app/student/myCoursesList")
    Call< StudentRegisterClass > getMyCoursesList(@Body StudentRegisterClass studentRegisterClass);

    @POST("/app/student/query")
    Call<StudentQueryClass > sendQuery(@Body StudentQueryClass studentQueryClass);


    @GET("app/ping")
    Call<StatusClass> getStatus();

    @POST("/app/old/message")
    Call<Messege[] > getPendingOldQueryList(@Body String queryid);

    @POST("/app/new/message")
    Call<Messege[] > getPendingNewQueryList(@Body String queryid);



}
