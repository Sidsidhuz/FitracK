package com.example.fitrack.models;

import java.io.Serializable;

public class Client implements Serializable {
    private String id;
    private String fullName;
    private int age;
    private String gender;
    private double height;
    private double weight;
    private String goal;
    private String phoneNumber;
    private String joinDate;
    private String profilePhotoUrl;
    private double attendancePercentage;
    private String lastWorkout;
    private int workoutStreak;
    private boolean isImportant;
    private int reminderHour = 8; 
    private int reminderMinute = 0;

    public Client() {
    }

    public Client(String id, String fullName, int age, String gender, double height, double weight, String goal,
            String phoneNumber, String joinDate, String profilePhotoUrl, double attendancePercentage,
            String lastWorkout, int workoutStreak) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
        this.phoneNumber = phoneNumber;
        this.joinDate = joinDate;
        this.profilePhotoUrl = profilePhotoUrl;
        this.attendancePercentage = attendancePercentage;
        this.lastWorkout = lastWorkout;
        this.workoutStreak = workoutStreak;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public String getLastWorkout() {
        return lastWorkout;
    }

    public void setLastWorkout(String lastWorkout) {
        this.lastWorkout = lastWorkout;
    }

    public int getWorkoutStreak() {
        return workoutStreak;
    }

    public void setWorkoutStreak(int workoutStreak) {
        this.workoutStreak = workoutStreak;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public void setReminderHour(int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public int getReminderMinute() {
        return reminderMinute;
    }

    public void setReminderMinute(int reminderMinute) {
        this.reminderMinute = reminderMinute;
    }
}

