package com.example.app_tim17.service;

import com.example.app_tim17.model.request.LoginRequest;
import com.example.app_tim17.model.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {

    @Headers({"Content-Type: application/json", "Accept: application/json", "Connection: close"})
    @POST("user/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
