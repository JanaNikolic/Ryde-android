package com.example.app_tim17.model.response.document;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DocumentResponse {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("documentImage")
    @Expose
    private String documentImage;
    @SerializedName("driverId")
    @Expose
    private Long driverId;

    public DocumentResponse() {
    }

    public DocumentResponse(Long id, String name, String documentImage, Long driverId) {
        super();
        this.id = id;
        this.name = name;
        this.documentImage = documentImage;
        this.driverId = driverId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

}
