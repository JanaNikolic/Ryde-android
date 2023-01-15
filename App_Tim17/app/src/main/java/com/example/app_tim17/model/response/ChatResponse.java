package com.example.app_tim17.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatResponse implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("chats")
    @Expose
    private List<Chat> chats = new ArrayList<Chat>();
    private final static long serialVersionUID = 4210379185177992559L;

    public ChatResponse() {
    }

    public ChatResponse(Integer count, List<Chat> chats) {
        super();
        this.count = count;
        this.chats = chats;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}
