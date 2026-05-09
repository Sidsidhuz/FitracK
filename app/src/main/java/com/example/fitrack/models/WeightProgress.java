package com.example.fitrack.models;

import java.io.Serializable;

public class WeightProgress implements Serializable {
    private String id;
    private String clientId;
    private String date;
    private double weight;

    public WeightProgress() {
    }

    public WeightProgress(String id, String clientId, String date, double weight) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.weight = weight;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

