package com.example.fitrack.models;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String id;
    private String clientId;
    private String day;
    private String focus;
    private String notes;

    public Schedule() {
    }

    public Schedule(String id, String clientId, String day, String focus, String notes) {
        this.id = id;
        this.clientId = clientId;
        this.day = day;
        this.focus = focus;
        this.notes = notes;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

