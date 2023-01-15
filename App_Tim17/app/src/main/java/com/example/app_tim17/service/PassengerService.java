package com.example.app_tim17.service;

import com.example.app_tim17.model.request.LoginRequest;
import com.example.app_tim17.model.request.UserRequest;
import com.example.app_tim17.model.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PassengerService {

    @POST("passenger")
    Call<UserRequest> register(@Body UserRequest userRequest);
}
