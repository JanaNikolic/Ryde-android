package com.example.app_tim17.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewRequest {

    @SerializedName("rating")
    @Expose
    int rating;

    @SerializedName("comment")
    @Expose
    String comment;
    public ReviewRequest() {

    }
    public ReviewRequest(int rating, String comment) {
        super();
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
