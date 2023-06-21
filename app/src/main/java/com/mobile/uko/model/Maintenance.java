package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Maintenance {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("ticketStatus")
    @Expose
    private String ticketStatus;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("ticketCreator")
    @Expose
    private TicketCreator ticketCreator;
    @SerializedName("ticketMaintenanceType")
    @Expose
    private TicketMaintenanceType ticketMaintenanceType;
    @SerializedName("ticketImages")
    @Expose
    private List<TicketImages> ticketImages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public TicketCreator getTicketCreator() {
        return ticketCreator;
    }

    public void setTicketCreator(TicketCreator ticketCreator) {
        this.ticketCreator = ticketCreator;
    }

    public TicketMaintenanceType getTicketMaintenanceType() {
        return ticketMaintenanceType;
    }

    public void setTicketMaintenanceType(TicketMaintenanceType ticketMaintenanceType) {
        this.ticketMaintenanceType = ticketMaintenanceType;
    }

    public List<TicketImages> getTicketImages() {
        return ticketImages;
    }

    public void setTicketImages(List<TicketImages> ticketImages) {
        this.ticketImages = ticketImages;
    }
}
