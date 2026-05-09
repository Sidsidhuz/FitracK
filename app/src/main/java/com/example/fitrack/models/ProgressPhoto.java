package com.example.fitrack.models;

import java.io.Serializable;

public class ProgressPhoto implements Serializable {
    private String id;
    private String clientId;
    private String title;
    private String imageUrl;
    private String capturedOn;
    private String type;

    public ProgressPhoto() {
    }

    public ProgressPhoto(String id, String clientId, String title, String imageUrl, String capturedOn, String type) {
        this.id = id;
        this.clientId = clientId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.capturedOn = capturedOn;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCapturedOn() {
        return capturedOn;
    }

    public void setCapturedOn(String capturedOn) {
        this.capturedOn = capturedOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

