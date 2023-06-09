package com.example.contactbook.service;

import com.example.contactbook.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostService {

    @GET("posts/")
    Call<List<Post>> getPosts();

    @POST("posts/")
    Call<Post> addUser(@Body Post post);

    @GET("posts/{id}")
    Call<Post> getUser(@Path("id") long id);
}
