package com.example.app_tim17.model.response.ride;

import java.util.List;

import com.example.app_tim17.model.response.Location;
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
    private Long totalCost;
    @SerializedName("driver")
    @Expose
    private DriverRideResponse driver;
    @SerializedName("passengers")
    @Expose
    private List<PassengerRideResponse> passengers = null;
    @SerializedName("estimatedTimeInMinutes")
    @Expose
    private Long estimatedTimeInMinutes;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("babyTransport")
    @Expose
    private Boolean babyTransport;
    @SerializedName("petTransport")
    @Expose
    private Boolean petTransport;
    @SerializedName("rejection")
    @Expose
    private Rejection rejection;
    @SerializedName("locations")
    @Expose
    private List<Location> locations = null;

    public Ride() {
    }

    public Ride(Long id, String startTime, String endTime, Long totalCost, DriverRideResponse driver, List<PassengerRideResponse> passengers, Long estimatedTimeInMinutes, String vehicleType, Boolean babyTransport, Boolean petTransport, Rejection rejection, List<Location> locations) {
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
        this.rejection = rejection;
        this.locations = locations;
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

    public Long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Long totalCost) {
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

    public Long getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Long estimatedTimeInMinutes) {
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

    public Rejection getRejection() {
        return rejection;
    }

    public void setRejection(Rejection rejection) {
        this.rejection = rejection;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}