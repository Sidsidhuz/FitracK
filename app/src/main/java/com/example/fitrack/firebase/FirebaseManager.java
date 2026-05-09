package com.example.fitrack.firebase;

import android.content.Context;

/**
 * Simple no-Firebase stub used for demo/local mode. Keeps the app runnable when
 * google-services.json and Firebase SDK are not available.
 */
public final class FirebaseManager {
    private static boolean configured = false;

    private FirebaseManager() {
    }

    public static void initialize(Context context) {
        // deliberately leave configured=false for local/demo mode
        configured = false;
    }

    public static boolean isConfigured() {
        return configured;
    }
}
