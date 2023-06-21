package com.mobile.uko.model;

import com.google.gson.annotations.SerializedName;

public class LoginToken {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("token")
    private String token;
    @SerializedName("isProfileVerified")
    private String isProfileVerified;
    @SerializedName("isProfileApproved")
    private String isProfileApproved;
    @SerializedName("propertyLocationId")
    private Integer propertyLocationId;
    @SerializedName("userStatus")
    private String userStatus;


    public LoginToken(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getIsProfileVerified() {
        return isProfileVerified;
    }

    public String getIsProfileApproved() {
        return isProfileApproved;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public Integer getPropertyLocationId() {
        return propertyLocationId;
    }
}
