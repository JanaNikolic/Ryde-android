package com.example.app_tim17.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {

    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;
    @SerializedName("date")
    @Expose(serialize = false)
    private String date;
    @SerializedName("message")
    @Expose
    private String message;

    public Note() {
    }

    public Note(Long id, String date, String message) {
        super();
        this.id = id;
        this.date = date;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
