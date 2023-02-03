package com.example.app_tim17.service;

import com.example.app_tim17.model.request.PassengerUpdateRequest;
import com.example.app_tim17.model.request.UserRequest;
import com.example.app_tim17.model.response.DistanceStatisticsResponse;
import com.example.app_tim17.model.response.MoneyStatisticsResponse;
import com.example.app_tim17.model.response.PassengerResponse;

import com.example.app_tim17.model.response.RideStatisticsResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.ride.RideResponse;

import com.example.app_tim17.model.response.DistanceStatisticsResponse;
import com.example.app_tim17.model.response.MoneyStatisticsResponse;
import com.example.app_tim17.model.response.RideStatisticsResponse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface PassengerService {

    @POST("passenger")
    Call<UserRequest> register(@Body UserRequest userRequest);

    @PUT("passenger/{id}")
    Call<PassengerUpdateRequest> updatePassenger(@Header("Authorization") String token, @Body PassengerUpdateRequest updateRequest, @Path("id") Long id);

    @GET("passenger/{id}")
    Call<PassengerResponse> getPassenger(@Header("Authorization") String token, @Path("id") Long id);


    @GET("passenger/{id}/ride")
    Call<RideResponse> getPassengerRides(@Header("Authorization") String token, @Path("id") Long id);


    @GET("passenger/rideCount/{id}")
    Call<RideStatisticsResponse> getRideCount(@Path("id") Long id, @Header("Authorization") String token,
                                              @Query("startDate") String startDate, @Query("endDate") String endDate);

    @GET("passenger/kilometers/{id}")
    Call<DistanceStatisticsResponse> getDistanceCount(@Path("id") Long id, @Header("Authorization") String token,
                                                      @Query("startDate") String startDate, @Query("endDate") String endDate);

    @GET("passenger/money/{id}")
    Call<MoneyStatisticsResponse> getMoneyCount(@Path("id") Long id, @Header("Authorization") String token,
                                                @Query("startDate") String startDate, @Query("endDate") String endDate);
}
