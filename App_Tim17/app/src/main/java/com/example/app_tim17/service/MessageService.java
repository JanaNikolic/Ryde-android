package com.example.app_tim17.service;

import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.message.MessagesResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
    @GET("user/{id}/message")
    Call<MessagesResponse> getMessages(@Path("id") Long id);

    @POST("user/{id}/message")
    Call<Message> sendMessage(@Path("id") Long id, @Header("Authorization") String token);
}
