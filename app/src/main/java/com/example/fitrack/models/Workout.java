package com.example.fitrack.models;

import java.io.Serializable;

public class Workout implements Serializable {
    private String id;
    private String clientId;
    private String category;
    private String exerciseName;
    private String date;
    private double weightUsed;
    private int sets;
    private int reps;
    private String notes;
    private boolean personalRecord;

    public Workout() {
    }

    public Workout(String id, String clientId, String category, String exerciseName, String date, double weightUsed,
            int sets, int reps, String notes, boolean personalRecord) {
        this.id = id;
        this.clientId = clientId;
        this.category = category;
        this.exerciseName = exerciseName;
        this.date = date;
        this.weightUsed = weightUsed;
        this.sets = sets;
        this.reps = reps;
        this.notes = notes;
        this.personalRecord = personalRecord;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getWeightUsed() {
        return weightUsed;
    }

    public void setWeightUsed(double weightUsed) {
        this.weightUsed = weightUsed;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isPersonalRecord() {
        return personalRecord;
    }

    public void setPersonalRecord(boolean personalRecord) {
        this.personalRecord = personalRecord;
    }
}

