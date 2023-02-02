package com.example.app_tim17.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewRequest {
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("comment")
    @Expose
    private String comment;

    public ReviewRequest() {
    }

    public ReviewRequest(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
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
}
