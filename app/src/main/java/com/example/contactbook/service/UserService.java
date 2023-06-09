package com.example.contactbook.service;

import com.example.contactbook.model.CheckPassword;
import com.example.contactbook.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Call<User> addUser(@Body User user);

    @POST("users/check-password")
    Call<CheckPassword> checkPassword(@Body CheckPassword checkPassword);

    @PUT("users/change-password")
    Call<Boolean> changePassword(@Body CheckPassword checkPassword);

    @GET("users/{code}")
    Call<User> getUser(@Path("code") String code);

}
