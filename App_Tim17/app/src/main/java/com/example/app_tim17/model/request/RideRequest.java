package com.example.app_tim17.model.request;

import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.ride.LocationForRide;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RideRequest {
    @SerializedName("locations")
    @Expose
    private List<LocationForRide> locations;
    @SerializedName("passengers")
    @Expose
    private List<PassengerRequest> passengers;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("babyTransport")
    @Expose
    private Boolean babyTransport;
    @SerializedName("petTransport")
    @Expose
    private Boolean petTransport;
    @SerializedName("scheduledTime")
    @Expose
    private String scheduledTime;

    public RideRequest(List<LocationForRide> locations, List<PassengerRequest> passengers, String vehicleType, Boolean babyTransport, Boolean petTransport, String scheduledTime) {
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.scheduledTime = scheduledTime;
    }

    public RideRequest() {
    }

    public List<LocationForRide> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationForRide> locations) {
        this.locations = locations;
    }

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
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

    public Boolean getPetTransport() {
        return petTransport;
    }

    public void setPetTransport(Boolean petTransport) {
        this.petTransport = petTransport;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "locations=" + locations +
                ", passengers=" + passengers +
                ", vehicleType='" + vehicleType + '\'' +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", scheduledTime='" + scheduledTime + '\'' +
                '}';
    }
}
