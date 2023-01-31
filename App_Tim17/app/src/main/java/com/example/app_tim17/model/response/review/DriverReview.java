package com.example.app_tim17.model.response.review;

import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverReview {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("rating")
    @Expose
    private Long rating;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("passenger")
    @Expose
    private PassengerRideResponse passenger;


    public DriverReview() {
    }

    public DriverReview(Long id, Long rating, String comment, PassengerRideResponse passenger) {
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

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
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
