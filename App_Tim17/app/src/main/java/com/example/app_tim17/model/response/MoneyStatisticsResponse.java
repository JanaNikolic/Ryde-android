package com.example.app_tim17.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MoneyStatisticsResponse {
    @SerializedName("money")
    @Expose
    private Map<String, Integer> money;

    @SerializedName("totalCount")
    @Expose
    private float totalCount;

    @SerializedName("averageCount")
    @Expose
    private float averageCount;


    public MoneyStatisticsResponse(Map<String, Integer> money, float totalCount, float averageCount) {
        this.money = money;
        this.totalCount = totalCount;
        this.averageCount = averageCount;
    }

    public MoneyStatisticsResponse() {
    }

    public Map<String, Integer> getKilometersByDay() {
        return money;
    }

    public void setKilometersByDay(Map<String, Integer> money) {
        this.money = money;
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
