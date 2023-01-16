package com.example.app_tim17.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordResetRequest {

    @SerializedName("new_password")
    @Expose
    private String newPassword;
    @SerializedName("code")
    @Expose
    private String code;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(String newPassword, String code) {
        super();
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
