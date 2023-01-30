package com.example.app_tim17.service;

import com.example.app_tim17.model.request.PassengerUpdateRequest;
import com.example.app_tim17.model.request.UserRequest;
import com.example.app_tim17.model.response.PassengerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PassengerService {

    @POST("passenger")
    Call<UserRequest> register(@Body UserRequest userRequest);

    @PUT("passenger/{id}")
    Call<PassengerUpdateRequest> updatePassenger(@Header("Authorization") String token, @Body PassengerUpdateRequest updateRequest, @Path("id") Long id);

    @GET("passenger/{id}")
    Call<PassengerResponse> getPassenger(@Header("Authorization") String token, @Path("id") Long id);
}
