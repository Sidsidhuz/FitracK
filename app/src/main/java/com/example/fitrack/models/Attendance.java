package com.example.fitrack.models;

import java.io.Serializable;

public class Attendance implements Serializable {
    private String id;
    private String clientId;
    private String date;
    private boolean present;
    private String notes;

    public Attendance() {
    }

    public Attendance(String id, String clientId, String date, boolean present, String notes) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.present = present;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

