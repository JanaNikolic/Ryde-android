
package com.example.app_tim17.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewRequest {

    @SerializedName("rating")
    @Expose
    float rating;

    @SerializedName("comment")
    @Expose
    String comment;
    public ReviewRequest() {

    }
    public ReviewRequest(float rating, String comment) {
        super();
        this.rating = rating;
        this.comment = comment;
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
}

