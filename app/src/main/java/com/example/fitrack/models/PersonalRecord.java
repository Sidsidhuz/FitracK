package com.example.fitrack.models;

import java.io.Serializable;

public class PersonalRecord implements Serializable {
    private String id;
    private String clientId;
    private String exerciseName;
    private double maxWeight;
    private String achievedOn;

    public PersonalRecord() {
    }

    public PersonalRecord(String id, String clientId, String exerciseName, double maxWeight, String achievedOn) {
        this.id = id;
        this.clientId = clientId;
        this.exerciseName = exerciseName;
        this.maxWeight = maxWeight;
        this.achievedOn = achievedOn;
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

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getAchievedOn() {
        return achievedOn;
    }

    public void setAchievedOn(String achievedOn) {
        this.achievedOn = achievedOn;
    }
}

