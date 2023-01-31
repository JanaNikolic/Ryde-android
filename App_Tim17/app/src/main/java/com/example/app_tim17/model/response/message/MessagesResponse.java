package com.example.app_tim17.model.response.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagesResponse implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("results")
    @Expose
    private List<Message> messages = new ArrayList<Message>();

    public MessagesResponse() {
    }

    public MessagesResponse(Integer count, List<Message> messages) {
        super();
        this.count = count;
        this.messages = messages;
    }

    public Integer getTotalCount() {
        return count;
    }

    public void setTotalCount(Integer count) {
        this.count = count;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "MessagesResponse{" +
                "count=" + count +
                ", messages=" + messages +
                '}';
    }
}