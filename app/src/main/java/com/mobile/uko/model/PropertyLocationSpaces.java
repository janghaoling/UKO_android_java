package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PropertyLocationSpaces implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("propertyLocationSpacesImages")
    @Expose
    private List<PropertyLocationSpaceImages> propertyLocationSpacesImages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PropertyLocationSpaceImages> getPropertyLocationSpacesImages() {
        return propertyLocationSpacesImages;
    }

    public void setPropertyLocationSpacesImages(List<PropertyLocationSpaceImages> propertyLocationSpacesImages) {
        this.propertyLocationSpacesImages = propertyLocationSpacesImages;
    }
}
