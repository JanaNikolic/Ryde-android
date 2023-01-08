package com.example.app_tim17.model.response.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverRideResponse {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("email")
    @Expose
    private String email;

    /**
     * No args constructor for use in serialization
     *
     */
    public DriverRideResponse() {
    }

    /**
     *
     * @param id
     * @param email
     */
    public DriverRideResponse(Long id, String email) {
        super();
        this.id = id;
        this.email = email;
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
