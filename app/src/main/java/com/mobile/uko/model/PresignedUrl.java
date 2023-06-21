package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PresignedUrl {
    @SerializedName("putUrl")
    @Expose
    private String putUrl;
    @SerializedName("getUrl")
    @Expose
    private String getUrl;

    public String getPutUrl() {
        return putUrl;
    }

    public void setPutUrl(String putUrl) {
        this.putUrl = putUrl;
    }

    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }
}
