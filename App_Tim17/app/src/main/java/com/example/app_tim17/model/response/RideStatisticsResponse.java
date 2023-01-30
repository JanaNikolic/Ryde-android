package com.example.app_tim17.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class RideStatisticsResponse {
    @SerializedName("countsByDay")
    @Expose
    private Map<String, Integer> countsByDay;

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    @SerializedName("averageCount")
    @Expose
    private float averageCount;


    public RideStatisticsResponse(Map<String, Integer> countsByDay, Integer totalCount, float averageCount) {
        this.countsByDay = countsByDay;
        this.totalCount = totalCount;
        this.averageCount = averageCount;
    }

    public RideStatisticsResponse() {
    }

    public Map<String, Integer> getCountsByDay() {
        return countsByDay;
    }

    public void setCountsByDay(Map<String, Integer> countsByDay) {
        this.countsByDay = countsByDay;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public float getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(float averageCount) {
        this.averageCount = averageCount;
    }
}
