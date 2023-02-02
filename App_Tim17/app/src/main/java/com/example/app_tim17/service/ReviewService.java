package com.example.app_tim17.service;



import com.example.app_tim17.model.request.ReviewRequest;
import com.example.app_tim17.model.request.UserRequest;
import com.example.app_tim17.model.response.review.Review;
import com.example.app_tim17.model.response.review.ReviewRideResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {
    @GET("review/{id}")
    Call<ReviewRideResponse> getRideReviews(@Header("Authorization") String token, @Path("id") Long id);

    @POST("review/{id}/driver")
    Call<Review> postDriverReview(@Header("Authorization") String token, @Path("id") Long id, @Body ReviewRequest reviewRequest);

    @POST("review/{id}/vehicle")
    Call<Review> postVehicleReview(@Header("Authorization") String token, @Path("id") Long id, @Body ReviewRequest reviewRequest);

}
