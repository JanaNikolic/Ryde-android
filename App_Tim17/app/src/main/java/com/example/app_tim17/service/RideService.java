package com.example.app_tim17.service;

import com.example.app_tim17.model.request.FavoriteRouteRequest;
import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.model.request.PanicRequest;
import com.example.app_tim17.model.request.RejectionRequest;
import com.example.app_tim17.model.request.RideRequest;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.message.MessagesResponse;

import com.example.app_tim17.model.response.ride.FavoriteRoute;

import com.example.app_tim17.model.response.ride.FavoriteRouteResponse;
import com.example.app_tim17.model.response.ride.Ride;

import kotlin.coroutines.jvm.internal.DebugMetadata;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<Ride> rejectRide(@Header("Authorization") String token, @Path("id") Long id, @Body RejectionRequest request);

    @GET("ride/driver/{driverId}/active")
    Call<Ride> getActiveRide(@Header("Authorization") String token, @Path("driverId") Long id);

    @PUT("ride/{id}/end")
    Call<Ride> endRide(@Header("Authorization") String token, @Path("id") Long id);

    @PUT("ride/{id}/start")
    Call<Ride> startRide(@Header("Authorization") String token, @Path("id") Long id);

    @PUT("ride/{id}/withdraw")
    Call<Ride> withdrawRide(@Header("Authorization") String token, @Path("id") Long id);

    @PUT("ride/{id}/panic")
    Call<Ride> panic(@Header("Authorization") String token, @Path("id") Long id, @Body PanicRequest request);

    @GET("ride/favorites")
    Call<FavoriteRouteResponse> getFavoriteRoutes(@Header("Authorization") String token);

    @DELETE("ride/favorites/{id}")
    Call<String> deleteFavoriteRoute(@Header("Authorization") String token, @Path("id")Long id);

    @GET("ride/{id}")
    Call<Ride> getRide(@Header("Authorization") String token, @Path("id")Long id);

    @POST("ride/favorites")
    Call<FavoriteRoute> createFavorite(@Header("Authorization") String token, @Body FavoriteRouteRequest request);

}
