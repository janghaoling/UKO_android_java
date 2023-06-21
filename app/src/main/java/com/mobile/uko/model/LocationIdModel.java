package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LocationIdModel implements Serializable {

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
    @SerializedName("propertyLocationSpaces")
    @Expose
    private List<PropertyLocationSpaces> propertyLocationSpaces;
    @SerializedName("propertiesLocationImages")
    @Expose
    private List<PropertiesLocationImages> propertiesLocationImages;
    @SerializedName("propertiesNearbyLocations")
    @Expose
    private List<PropertiesNearbyLocations> propertiesNearbyLocations;
    @SerializedName("propertyManager")
    @Expose
    private PropertyManager propertyManager;


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

    public List<PropertiesNearbyLocations> getPropertiesNearbyLocations() {
        return propertiesNearbyLocations;
    }

    public void setPropertiesNearbyLocations(List<PropertiesNearbyLocations> propertiesNearbyLocations) {
        this.propertiesNearbyLocations = propertiesNearbyLocations;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    public List<PropertyLocationSpaces> getPropertyLocationSpaces() {
        return propertyLocationSpaces;
    }

    public void setPropertyLocationSpaces(List<PropertyLocationSpaces> propertyLocationSpaces) {
        this.propertyLocationSpaces = propertyLocationSpaces;
    }
}
