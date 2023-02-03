package com.example.app_tim17.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PasswordChangeRequest {

    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;

    public PasswordChangeRequest() {
    }

    public PasswordChangeRequest(String newPassword, String oldPassword) {
        super();
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

}
