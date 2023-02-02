package com.example.app_tim17.model.response.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewRideResponse {
    @SerializedName("vehicleReview")
    @Expose
    private List<Review> vehicleReview;
    @SerializedName("driverReview")
    @Expose
    private List<Review> driverReview;

    public ReviewRideResponse() {
    }

    public ReviewRideResponse(List<Review> vehicleReview, List<Review> driverReview) {
        this.vehicleReview = vehicleReview;
        this.driverReview = driverReview;
    }

    public List<Review> getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(List<Review> vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public List<Review> getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(List<Review> driverReview) {
        this.driverReview = driverReview;
    }
}
