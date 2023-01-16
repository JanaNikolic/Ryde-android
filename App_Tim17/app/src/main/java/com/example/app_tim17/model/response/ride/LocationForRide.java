package com.example.app_tim17.model.response.ride;

import com.example.app_tim17.model.response.Location;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationForRide {

    @SerializedName("departure")
    @Expose
    private Location departure;
    @SerializedName("destination")
    @Expose
    private Location destination;

    public LocationForRide() {
    }

    public LocationForRide(Location departure, Location destination) {
        super();
        this.departure = departure;
        this.destination = destination;
    }

    public Location getDeparture() {
        return departure;
    }

    public void setDeparture(Location departure) {
        this.departure = departure;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
