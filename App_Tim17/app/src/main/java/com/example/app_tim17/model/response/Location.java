package com.example.app_tim17.model.response;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Location {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    @Nullable
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    @Nullable
    private Double longitude;

    public Location() {
    }

    public Location(String address, Double latitude, Double longitude) {
        super();
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}