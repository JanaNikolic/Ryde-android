
//public class Departure {
//
//    @SerializedName("address")
//    @Expose
//    private String address;
//    @SerializedName("latitude")
//    @Expose
//    private Double latitude;
//    @SerializedName("longitude")
//    @Expose
//    private Double longitude;
//
//    public Departure() {
//    }
//
//    public Departure(String address, Double latitude, Double longitude) {
//        super();
//        this.address = address;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }
//
//}
//

//public class Destination {
//
//    @SerializedName("address")
//    @Expose
//    private String address;
//    @SerializedName("latitude")
//    @Expose
//    private Double latitude;
//    @SerializedName("longitude")
//    @Expose
//    private Double longitude;
//
//    public Destination() {
//    }
//
//    public Destination(String address, Double latitude, Double longitude) {
//        super();
//        this.address = address;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }
//
//}
//-----------------------------------com.example.app_tim17.model.Driver.java-----------------------------------
//

//

//
//
//        package com.example.app_tim17.model;
//
//
//        import com.google.gson.annotations.Expose;
//        import com.google.gson.annotations.SerializedName;
//
//
//public class Location {
//

//
//}
//







package com.example.app_tim17.model.response.ride;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RideResponse {

    @SerializedName("totalCount")
    @Expose
    private Long totalCount;
    @SerializedName("results")
    @Expose
    private List<Ride> results = null;

    public RideResponse() {
    }

    public RideResponse(Long totalCount, List<Ride> results) {
        super();
        this.totalCount = totalCount;
        this.results = results;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<Ride> getRides() {
        return results;
    }

    public void setRides(List<Ride> results) {
        this.results = results;
    }

}
