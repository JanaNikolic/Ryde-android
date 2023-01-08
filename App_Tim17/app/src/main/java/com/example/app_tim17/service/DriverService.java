package com.example.app_tim17.service;

import com.example.app_tim17.model.DriverResponse;
import com.example.app_tim17.model.VehicleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DriverService {

    @GET("driver/{id}")
    Call<DriverResponse> getDriver(@Path("id") Long id);

    @GET("driver/{id}/vehicle")
    Call<VehicleResponse> getDriversVehicle(@Path("id") Long id);
}
