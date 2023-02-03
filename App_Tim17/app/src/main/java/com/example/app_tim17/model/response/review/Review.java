package com.example.app_tim17.model.response.review;

import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("passenger")
    @Expose
    private PassengerRideResponse passenger;

    public Review() {
    }

    public Review(Long id, int rating, String comment, PassengerRideResponse passenger) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
