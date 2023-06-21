package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsAll {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("venueDateTime")
    @Expose
    private String venueDateTime;
    @SerializedName("venueToDateTime")
    @Expose
    private String venueToDateTime;
    @SerializedName("eventImage")
    @Expose
    private String eventImage;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVenueDateTime() {
        return venueDateTime;
    }

    public String getVenueToDateTime() {
        return venueToDateTime;
    }

    public String getEventImage() {
        return eventImage;
    }
}
