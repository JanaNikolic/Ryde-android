package com.example.app_tim17.model.response.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RideReviewsResponse {

    @SerializedName("vehicleReview")
    @Expose
    private List<VehicleReview> vehicleReview;
    @SerializedName("driverReview")
    @Expose
    private List<DriverReview> driverReview;

    public RideReviewsResponse() {
    }

    public RideReviewsResponse(List<VehicleReview> vehicleReview, List<DriverReview> driverReview) {
        super();
        this.vehicleReview = vehicleReview;
        this.driverReview = driverReview;
    }

    public List<VehicleReview> getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(List<VehicleReview> vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public List<DriverReview> getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(List<DriverReview> driverReview) {
        this.driverReview = driverReview;
    }

}