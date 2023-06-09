package com.example.contactbook.service;

import com.example.contactbook.model.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ScheduleService {
    @GET("schedules/teacher/{code}")
    Call<List<Schedule>> getScheduleByTeacher(@Path("code") String code);

    @GET("schedules/find-by-class/{className}")
    Call<List<Schedule>> getScheduleByClass(@Path("className") String className);

    @GET("schedules/")
    Call<List<Schedule>> getAllSchedules();

    @GET("schedules/{id}")
    Call<Schedule> getScheduleById(@Path("id") long scheduleId);
}
