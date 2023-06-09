package com.example.contactbook.service;

import com.example.contactbook.model.Mark;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MarkService {

    @GET("marks/by-student/{code}")
    Call<List<Mark>> getMarksByStudent(@Path("code") String code);

}
