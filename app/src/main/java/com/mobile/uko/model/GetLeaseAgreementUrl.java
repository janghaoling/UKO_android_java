package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLeaseAgreementUrl {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("presignedUrl")
    @Expose
    private PresignedUrl presignedUrl;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PresignedUrl getPresignedUrl() {
        return presignedUrl;
    }

    public void setPresignedUrl(PresignedUrl presignedUrl) {
        this.presignedUrl = presignedUrl;
    }
}
