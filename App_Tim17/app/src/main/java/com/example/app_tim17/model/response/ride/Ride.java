package com.example.app_tim17.model.response.ride;


import androidx.annotation.Nullable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Ride {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("totalCost")
    @Expose
    private Integer totalCost;
    @SerializedName("driver")
    @Expose
    private DriverRideResponse driver;
    @SerializedName("passengers")
    @Expose
    private List<PassengerRideResponse> passengers = null;
    @SerializedName("estimatedTimeInMinutes")
    @Expose
    private Integer estimatedTimeInMinutes;
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
    @SerializedName("rejection")
    @Expose
    private RejectionResponse rejectionResponse;
    @SerializedName("locations")
    @Expose
    private List<LocationForRide> locations;



    @SerializedName("status")
    @Expose
    private String status;

    public Ride() {
    }

    public Ride(Long id, String startTime, String endTime, Integer totalCost, DriverRideResponse driver, List<PassengerRideResponse> passengers, Integer estimatedTimeInMinutes, String vehicleType, Boolean babyTransport, Boolean petTransport, RejectionResponse rejectionResponse, List<LocationForRide> locations, String status) {
        super();
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = driver;
        this.passengers = passengers;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.rejectionResponse = rejectionResponse;
        this.locations = locations;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public DriverRideResponse getDriver() {
        return driver;
    }

    public void setDriver(DriverRideResponse driver) {
        this.driver = driver;
    }

    public List<PassengerRideResponse> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRideResponse> passengers) {
        this.passengers = passengers;
    }

    public Integer getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Integer estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
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

    public RejectionResponse getRejection() {
        return rejectionResponse;
    }

    public void setRejection(RejectionResponse rejectionResponse) {
        this.rejectionResponse = rejectionResponse;
    }

    public List<LocationForRide> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationForRide> locations) {
        this.locations = locations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}