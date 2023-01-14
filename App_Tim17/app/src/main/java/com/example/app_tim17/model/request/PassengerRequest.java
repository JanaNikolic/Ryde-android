package com.example.app_tim17.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassengerRequest {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("email")
    @Expose
    private String email;

    public PassengerRequest(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public PassengerRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
