package com.example.app_tim17.model.response.vehicle;

import com.example.app_tim17.model.response.Location;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VehicleResponse {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("driverId")
    @Expose
    private Long driverId;
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
    private Location location;
    @SerializedName("passengerSeats")
    @Expose
    private Long passengerSeats;
    @SerializedName("babyTransport")
    @Expose
    private Boolean babyTransport;
    @SerializedName("petTransport")
    @Expose
    private Boolean petTransport;

    /**
     * No args constructor for use in serialization
     *
     */
    public VehicleResponse() {
    }

    public VehicleResponse(Long id, Long driverId, String vehicleType, String model, String licenseNumber, Location location, Long passengerSeats, Boolean babyTransport, Boolean petTransport) {
        super();
        this.id = id;
        this.driverId = driverId;
        this.vehicleType = vehicleType;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.location = location;
        this.passengerSeats = passengerSeats;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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
        return location;
    }

    public void setCurrentLocation(Location location) {
        this.location = location;
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
