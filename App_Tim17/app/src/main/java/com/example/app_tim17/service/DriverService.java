package com.example.app_tim17.service;

import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DriverService {

    @Headers({"Content-Type: application/json", "Accept: application/json", "Connection: keep-alive"})
    @GET("driver/{id}")
    Call<DriverResponse> getDriver(@Path("id") Long id);

    @Headers({"Content-Type: application/json", "Accept: application/json", "Connection: keep-alive"})
    @GET("driver/{id}/vehicle")
    Call<VehicleResponse> getDriversVehicle(@Path("id") Long id);
}
