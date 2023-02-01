package com.example.app_tim17.model.response.review;

import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverReview {

    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("passenger")
    @Expose(serialize = false)
    private PassengerRideResponse passenger;


    public DriverReview() {
    }

    public DriverReview(Long id, float rating, String comment, PassengerRideResponse passenger) {
        super();
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PassengerRideResponse getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerRideResponse passenger) {
        this.passenger = passenger;
    }

}
