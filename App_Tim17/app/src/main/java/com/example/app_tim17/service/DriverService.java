package com.example.app_tim17.service;

import com.example.app_tim17.model.WorkingHour;
import com.example.app_tim17.model.request.DriverUpdateRequest;
import com.example.app_tim17.model.request.EndWorkingHour;
import com.example.app_tim17.model.request.StartWorkingHour;
import com.example.app_tim17.model.response.DistanceStatisticsResponse;
import com.example.app_tim17.model.response.MoneyStatisticsResponse;
import com.example.app_tim17.model.response.RideStatisticsResponse;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverService {

    @GET("driver/{id}")
    Call<DriverResponse> getDriver(@Path("id") Long id, @Header("Authorization") String token);

    @GET("driver/{id}/vehicle")
    Call<VehicleResponse> getDriversVehicle(@Path("id") Long id, @Header("Authorization") String token);

    @GET("driver/{id}/ride")
    Call<RideResponse> getDriversRides(@Path("id") Long id, @Header("Authorization") String token);

    @GET("driver/rideCount/{id}")
    Call<RideStatisticsResponse> getRideCount(@Path("id") Long id, @Header("Authorization") String token,
                                              @Query("startDate") String startDate, @Query("endDate") String endDate);

    @GET("driver/kilometers/{id}")
    Call<DistanceStatisticsResponse> getDistanceCount(@Path("id") Long id, @Header("Authorization") String token,
                                                      @Query("startDate") String startDate, @Query("endDate") String endDate);

    @GET("driver/money/{id}")
    Call<MoneyStatisticsResponse> getMoneyCount(@Path("id") Long id, @Header("Authorization") String token,
                                                @Query("startDate") String startDate, @Query("endDate") String endDate);

    @POST("driver/updateRequest")
    Call<String> driverUpdateRequest(@Header("Authorization") String token, @Body DriverUpdateRequest updateRequest);

    @POST("driver/{id}/working-hour")
    Call<WorkingHour> startShift(@Header("Authorization") String token, @Path("id") Long id, @Body StartWorkingHour workingHour);

    @PUT("driver/working-hour/{id}")
    Call<WorkingHour> endShift(@Header("Authorization") String token, @Path("id") Long id, @Body EndWorkingHour workingHour);

}
