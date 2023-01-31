package com.example.app_tim17.service;

import com.example.app_tim17.fragments.ChangePasswordFragment;
import com.example.app_tim17.model.request.LoginRequest;
import com.example.app_tim17.model.request.PasswordChangeRequest;
import com.example.app_tim17.model.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("user/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @PUT("user/{id}/changePassword")
    Call<String> changePassword(@Body PasswordChangeRequest request, @Header("Authorization") String token, @Path("id") Long id);
}
