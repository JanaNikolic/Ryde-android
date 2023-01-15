package com.example.app_tim17.model.response.chat;

import com.example.app_tim17.model.response.UserResponse;
import com.example.app_tim17.model.response.message.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Chat implements Serializable {
    @SerializedName("user")
    @Expose
    private UserResponse user;
    @SerializedName("lastMessage")
    @Expose
    private Message lastMessage;

    public Chat(UserResponse user, Message lastMessage) {
        super();
        this.user = user;
        this.lastMessage = lastMessage;
    }

    public Chat() {
    }


    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
