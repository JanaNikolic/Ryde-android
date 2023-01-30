package com.example.app_tim17.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.time.LocalDateTime;

public class StartWorkingHour {
    @Expose
    @SerializedName("start")
    private String start;

    public StartWorkingHour() {
    }

    public StartWorkingHour(String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
