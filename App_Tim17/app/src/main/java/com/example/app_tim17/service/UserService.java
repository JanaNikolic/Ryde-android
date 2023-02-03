package com.example.app_tim17.service;

import com.example.app_tim17.fragments.ChangePasswordFragment;
import com.example.app_tim17.model.Note;
import com.example.app_tim17.model.request.LoginRequest;
import com.example.app_tim17.model.request.PasswordChangeRequest;
import com.example.app_tim17.model.request.ResetPasswordRequest;
import com.example.app_tim17.model.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("user/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @PUT("user/{id}/changePassword")
    Call<String> changePassword(@Body PasswordChangeRequest request, @Header("Authorization") String token, @Path("id") Long id);

    @GET("user/{email}/resetPassword")
    Call<String> resetCode(@Path("email") String email);

    @PUT("user/{email}/resetPassword")
    Call<String> resetPassword(@Path("email") String email, @Body ResetPasswordRequest body);

    @POST("user/{id}/note")
    Call<Note> sendNote(@Path("id") Long id, @Body Note note,  @Header("Authorization") String token);
}
