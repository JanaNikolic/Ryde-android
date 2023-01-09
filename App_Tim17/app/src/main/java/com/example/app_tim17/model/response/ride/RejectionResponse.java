package com.example.app_tim17.model.response.ride;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;


public class RejectionResponse {

    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("timeOfRejection")
    @Expose
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timeOfRejection;

    public RejectionResponse() {
    }

    public RejectionResponse(String reason, LocalDateTime timeOfRejection) {
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

    public LocalDateTime getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(LocalDateTime timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }

}