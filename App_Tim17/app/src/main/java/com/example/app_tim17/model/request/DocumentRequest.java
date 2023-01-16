package com.example.app_tim17.model.request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DocumentRequest {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("documentImage")
    @Expose
    private String documentImage;

    public DocumentRequest() {
    }

    public DocumentRequest(String name, String documentImage) {
        super();
        this.name = name;
        this.documentImage = documentImage;
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

}
