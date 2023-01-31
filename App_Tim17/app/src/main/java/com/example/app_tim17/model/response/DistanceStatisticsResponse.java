package com.example.app_tim17.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class DistanceStatisticsResponse {

    @SerializedName("kilometersByDay")
    @Expose
    private Map<String, Float> kilometersByDay;

    @SerializedName("totalCount")
    @Expose
    private float totalCount;

    @SerializedName("averageCount")
    @Expose
    private float averageCount;


    public DistanceStatisticsResponse(Map<String, Float> kilometersByDay, float totalCount, float averageCount) {
        this.kilometersByDay = kilometersByDay;
        this.totalCount = totalCount;
        this.averageCount = averageCount;
    }

    public DistanceStatisticsResponse() {
    }

    public Map<String, Float> getKilometersByDay() {
        return kilometersByDay;
    }

    public void setKilometersByDay(Map<String, Float> kilometersByDay) {
        this.kilometersByDay = kilometersByDay;
    }

    public float getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(float totalCount) {
        this.totalCount = totalCount;
    }

    public float getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(float averageCount) {
        this.averageCount = averageCount;
    }
}
