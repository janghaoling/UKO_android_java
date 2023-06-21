package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationAllModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;
    @SerializedName("propertiesLocationImages")
    @Expose
    private List<PropertiesLocationImages> propertiesLocationImages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public List<PropertiesLocationImages> getPropertiesLocationImages() {
        return propertiesLocationImages;
    }

    public void setPropertiesLocationImages(List<PropertiesLocationImages> propertiesLocationImages) {
        this.propertiesLocationImages = propertiesLocationImages;
    }
}
