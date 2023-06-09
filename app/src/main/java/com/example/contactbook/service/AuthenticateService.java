package com.example.contactbook.service;

import com.example.contactbook.model.JwtRequest;
import com.example.contactbook.model.JwtResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticateService {
    @POST("authenticate")
    Call<JwtResponse> login(@Body JwtRequest request);
}
