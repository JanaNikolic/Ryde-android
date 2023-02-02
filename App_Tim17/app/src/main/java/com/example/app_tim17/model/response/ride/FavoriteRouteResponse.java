package com.example.app_tim17.model.response.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRouteResponse {
    @SerializedName("totalCount")
    @Expose
    private Long totalCount;
    @SerializedName("results")
    @Expose
    private List<FavoriteRoute> results = new ArrayList<FavoriteRoute>();

    public FavoriteRouteResponse() {
    }

    public FavoriteRouteResponse(Long totalCount, List<FavoriteRoute> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<FavoriteRoute> getResults() {
        return results;
    }

    public void setResults(List<FavoriteRoute> results) {
        this.results = results;
    }
}
