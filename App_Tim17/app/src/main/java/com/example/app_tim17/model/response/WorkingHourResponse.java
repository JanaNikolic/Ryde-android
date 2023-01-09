package com.example.app_tim17.model.response;

import java.util.List;

import com.example.app_tim17.model.WorkingHour;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkingHourResponse {

    @SerializedName("totalCount")
    @Expose
    private Long totalCount;
    @SerializedName("results")
    @Expose
    private List<WorkingHour> results = null;

    public WorkingHourResponse() {
    }

    public WorkingHourResponse(Long totalCount, List<WorkingHour> results) {
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

    public List<WorkingHour> getWorkingHours() {
        return results;
    }

    public void setWorkingHours(List<WorkingHour> results) {
        this.results = results;
    }

}
