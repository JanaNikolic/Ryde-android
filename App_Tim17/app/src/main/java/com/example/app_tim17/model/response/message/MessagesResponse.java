package com.example.app_tim17.model.response.message;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagesResponse {

    @SerializedName("totalCount")
    @Expose
    private Long totalCount;
    @SerializedName("results")
    @Expose
    private List<Message> results = null;

    public MessagesResponse() {
    }

    public MessagesResponse(Long totalCount, List<Message> results) {
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

    public List<Message> getMessages() {
        return results;
    }

    public void setMessages(List<Message> results) {
        this.results = results;
    }

}