package com.example.app_tim17.service;

import com.example.app_tim17.model.request.LoginRequest;
import com.example.app_tim17.model.request.UserRequest;
import com.example.app_tim17.model.response.LoginResponse;
import com.example.app_tim17.model.response.PassengerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PassengerService {

    @POST("passenger")
    Call<UserRequest> register(@Body UserRequest userRequest);

    @GET("passenger/{id}")
    Call<PassengerResponse> getPassenger(@Path("id") Long id, @Header("Authorization") String token);
}
