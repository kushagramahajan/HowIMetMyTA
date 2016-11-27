package com.example.kushagra.meetupapp.network.api;

import com.example.kushagra.meetupapp.Message;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.db.objects.RecentMessages;
import com.example.kushagra.meetupapp.network.model.StatusClass;
import com.example.kushagra.meetupapp.network.model.StudentQueryClass;
import com.example.kushagra.meetupapp.network.model.StudentRegisterClass;
import com.example.kushagra.meetupapp.network.model.TaNewMessage;

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


    @POST("/app/student/query")
    Call<Message> sendMessage(@Body Message messageObject);



    @POST("app/ping")
    Call<StatusClass> getStatus(@Body StatusClass studentId);

    @POST("/app/undeliveredChat")
    Call<RecentMessages> getPendingOldQueryList(@Body RecentMessages recentMessages);

    @POST("/app/ta/fetch")
    Call<TaNewMessage > getPendingNewQueryList(@Body TaNewMessage queryid);



}
