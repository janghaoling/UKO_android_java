package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaseAgreementHome {
    @SerializedName("userStatus")
    @Expose
    private String userStatus;
    @SerializedName("propertyDescription")
    @Expose
    private String propertyDescription;
    @SerializedName("propertyImage")
    @Expose
    private String propertyImage;
    @SerializedName("amountDue")
    @Expose
    private Float amountDue;

    public String getUserStatus() {
        return userStatus;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public String getPropertyImage() {
        return propertyImage;
    }

    public Float getAmountDue() {
        return amountDue;
    }
}
