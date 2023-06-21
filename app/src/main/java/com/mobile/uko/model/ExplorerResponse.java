package com.mobile.uko.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExplorerResponse {
    @SerializedName("propertiesLocations")
    @Expose
    private List<PropertiesLocations> propertiesLocations;
    @SerializedName("community")
    @Expose
    private Community community;
    @SerializedName("intro")
    @Expose
    private Intro intro;
    @SerializedName("spaces")
    @Expose
    private List<Spaces> spaces;
    @SerializedName("podcast")
    @Expose
    private List<Podcast> podcast;

    public List<PropertiesLocations> getPropertiesLocations() {
        return propertiesLocations;
    }

    public void setPropertiesLocations(List<PropertiesLocations> propertiesLocations) {
        this.propertiesLocations = propertiesLocations;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Intro getIntro() {
        return intro;
    }

    public void setIntro(Intro intro) {
        this.intro = intro;
    }

    public List<Spaces> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Spaces> spaces) {
        this.spaces = spaces;
    }

    public List<Podcast> getPodcast() {
        return podcast;
    }

    public void setPodcast(List<Podcast> podcast) {
        this.podcast = podcast;
    }
}
