package com.example.app_tim17.service;

import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DriverService {

    @GET("driver/{id}")
    Call<DriverResponse> getDriver(@Path("id") Long id, @Header("Authorization") String token);

    @GET("driver/{id}/vehicle")
    Call<VehicleResponse> getDriversVehicle(@Path("id") Long id, @Header("Authorization") String token);

    @GET("driver/{id}/ride")
    Call<RideResponse> getDriversRides(@Path("id") Long id, @Header("Authorization") String token);
}
