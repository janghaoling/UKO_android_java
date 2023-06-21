package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertiesLocations {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;
    @SerializedName("propertiesLocationImages")
    @Expose
    private List<PropertiesLocationImages> propertiesLocationImages;
    @SerializedName("propertyStatus")
    @Expose
    private String propertyStatus;

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

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }
}
