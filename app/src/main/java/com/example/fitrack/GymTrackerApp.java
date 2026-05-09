package com.example.fitrack;

import android.app.Application;

import com.example.fitrack.firebase.FirebaseManager;
import com.example.fitrack.local.LocalStore;

public class GymTrackerApp extends Application {
    private static GymTrackerApp instance;

    public static GymTrackerApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LocalStore.initialize(this);
        FirebaseManager.initialize(this);
    }
}
