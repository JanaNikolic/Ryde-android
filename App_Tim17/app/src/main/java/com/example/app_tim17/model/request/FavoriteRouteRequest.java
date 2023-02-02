package com.example.app_tim17.model.request;

import androidx.annotation.Nullable;

import com.example.app_tim17.model.response.ride.LocationForRide;
import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteRouteRequest {
    @SerializedName("favoriteName")
    @Expose
    private String favoriteName;
    @SerializedName("passengers")
    @Expose
    private List<PassengerRideResponse> passengers = null;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("babyTransport")
    @Expose
    private Boolean babyTransport;
    @SerializedName("petTransport")
    @Expose
    @Nullable
    private Boolean petTransport;
    @SerializedName("locations")
    @Expose
    private List<LocationForRide> locations;

    public FavoriteRouteRequest() {
    }

    public FavoriteRouteRequest(String favoriteName, List<PassengerRideResponse> passengers, String vehicleType, Boolean babyTransport, @Nullable Boolean petTransport, List<LocationForRide> locations) {
        this.favoriteName = favoriteName;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.locations = locations;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public List<PassengerRideResponse> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRideResponse> passengers) {
        this.passengers = passengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(Boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    @Nullable
    public Boolean getPetTransport() {
        return petTransport;
    }

    public void setPetTransport(@Nullable Boolean petTransport) {
        this.petTransport = petTransport;
    }

    public List<LocationForRide> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationForRide> locations) {
        this.locations = locations;
    }
}
