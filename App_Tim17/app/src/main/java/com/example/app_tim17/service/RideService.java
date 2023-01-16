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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RideService {

    @POST("ride")
    Call<Ride> createRide(@Header("Authorization") String token, @Body RideRequest rideRequest);

    @PUT("ride/{id}/accept")
    Call<Ride> acceptRide(@Header("Authorization") String token, @Path("id") Long id);

    @PUT("ride/{id}/cancel")
    Call<Ride> rejectRide(@Header("Authorization") String token, @Path("id") Long id);

    @GET("ride/driver/{driverId}/active")
    Call<Ride> getActiveRide(@Header("Authorization") String token, @Path("driverId") Long id);

    @PUT("ride/{id}/end")
    Call<Ride> endRide(@Header("Authorization") String token, @Path("id") Long id);
}
