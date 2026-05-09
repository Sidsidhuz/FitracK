package com.example.fitrack;

import android.app.Application;

import com.example.fitrack.firebase.FirebaseManager;

public class GymTrackerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseManager.initialize(this);
    }
}

