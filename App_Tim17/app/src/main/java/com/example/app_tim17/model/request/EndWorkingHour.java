package com.example.app_tim17.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class EndWorkingHour {

    @Expose
    @SerializedName("end")
    private String end;

    public EndWorkingHour() {
    }

    public EndWorkingHour(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
