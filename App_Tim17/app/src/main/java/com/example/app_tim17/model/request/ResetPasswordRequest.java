package com.example.app_tim17.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {

    @SerializedName("newPassword")
    @Expose
    private String newPassword;

    @SerializedName("code")
    @Expose
    private String code;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String newPassword, String code) {
        this.newPassword = newPassword;
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
