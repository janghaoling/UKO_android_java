package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsIdModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("veneue")
    @Expose
    private String veneue;
    @SerializedName("venueDateTime")
    @Expose
    private String venueDateTime;
    @SerializedName("venueToDateTime")
    @Expose
    private String venueToDateTime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("eventStatus")
    @Expose
    private String eventStatus;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVeneue() {
        return veneue;
    }

    public String getVenueDateTime() {
        return venueDateTime;
    }

    public String getVenueToDateTime() {
        return venueToDateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getEventStatus() {
        return eventStatus;
    }
}
