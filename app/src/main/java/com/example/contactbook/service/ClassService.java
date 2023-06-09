package com.example.contactbook.service;

import com.example.contactbook.model.ClassModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ClassService {

    @GET("classes/")
    Call<List<ClassModel>> getClasses();

    @GET("classes/find-by/{name}")
    Call<ClassModel> getClassByName(@Path("name") String name);

    @GET("classes/find-by-teacher/{code}")
    Call<ClassModel> getClassByTeacher(@Path("code") String code);

    @GET("classes/{id}")
    Call<ClassModel> getClassById(@Path("id") long id);
}
