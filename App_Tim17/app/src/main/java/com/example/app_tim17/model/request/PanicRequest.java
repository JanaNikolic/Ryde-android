package com.example.app_tim17.model.request;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanicRequest {
    @SerializedName("reason")
    @Expose
    @Nullable
    private String reason;

    public PanicRequest(String reason) {
        this.reason = reason;
    }

    public PanicRequest() {
        reason = "";
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
