package com.mobile.uko.model;

public class EventStatusUpdate {
    private String status;
    private Integer eventId;

    public EventStatusUpdate(String status, Integer eventId) {
        this.status = status;
        this.eventId = eventId;
    }
}
