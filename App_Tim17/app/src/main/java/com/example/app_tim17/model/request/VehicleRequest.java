package com.example.app_tim17.model.request;


import com.example.app_tim17.model.response.Location;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VehicleRequest {

    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("licenseNumber")
    @Expose
    private String licenseNumber;
    @SerializedName("currentLocation")
    @Expose
    private Location currentLocation;
    @SerializedName("passengerSeats")
    @Expose
    private Long passengerSeats;
    @SerializedName("babyTransport")
    @Expose
    private Boolean babyTransport;
    @SerializedName("petTransport")
    @Expose
    private Boolean petTransport;


    public VehicleRequest() {
    }

    public VehicleRequest(String vehicleType, String model, String licenseNumber, Location currentLocation, Long passengerSeats, Boolean babyTransport, Boolean petTransport) {
        super();
        this.vehicleType = vehicleType;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.currentLocation = currentLocation;
        this.passengerSeats = passengerSeats;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Long getPassengerSeats() {
        return passengerSeats;
    }

    public void setPassengerSeats(Long passengerSeats) {
        this.passengerSeats = passengerSeats;
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

}
