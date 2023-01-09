package com.example.app_tim17.model.request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RejectionRequest {

    @SerializedName("reason")
    @Expose
    private String reason;

    public RejectionRequest() {
    }

    public RejectionRequest(String reason) {
        super();
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}