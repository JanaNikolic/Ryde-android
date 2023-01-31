package com.example.app_tim17.service;

import com.example.app_tim17.model.request.ReviewRequest;
import com.example.app_tim17.model.response.review.DriverReview;
import com.example.app_tim17.model.response.review.RideReviewsResponse;
import com.example.app_tim17.model.response.review.VehicleReview;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {

    @GET("review/{rideId}")
    Call<RideReviewsResponse> getRideReviews(@Header("Authorization") String token, @Path("rideId") Long id);

    @POST("review/{rideId}/driver")
    Call<DriverReview> postDriverReview(@Header("Authorization") String token, @Path("rideId") Long id, @Body ReviewRequest review);

    @POST("review/{rideId}/vehicle")
    Call<VehicleReview> postVehicleReview(@Header("Authorization") String token, @Path("rideId") Long id, @Body ReviewRequest review);
}
