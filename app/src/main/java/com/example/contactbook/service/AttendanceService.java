package com.example.contactbook.service;

import com.example.contactbook.model.Attendance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AttendanceService {

    @GET("attendances/check-attend/{userCode}/{scheduleId}")
    Call<Boolean> checkAttend(@Path("userCode") String code, @Path("scheduleId") long scheduleId);

    @GET("attendances/get-by/{userCode}")
    Call<List<Attendance>> getByUser(@Path("userCode") String code);
}
