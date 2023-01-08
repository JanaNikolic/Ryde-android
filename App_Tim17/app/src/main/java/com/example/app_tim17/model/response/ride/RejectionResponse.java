package com.example.app_tim17.model.response.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Rejection {

    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("timeOfRejection")
    @Expose
    private String timeOfRejection;

    public Rejection() {
    }

    public Rejection(String reason, String timeOfRejection) {
        super();
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(String timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }

}