package com.example.app_tim17.service;

import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.model.request.RideRequest;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.message.MessagesResponse;
import com.example.app_tim17.model.response.ride.Ride;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RideService {

    @POST("ride")
    Call<Ride> createRide(@Header("Authorization") String token, @Body RideRequest rideRequest);
}
