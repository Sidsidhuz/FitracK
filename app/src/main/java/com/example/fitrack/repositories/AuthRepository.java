package com.example.fitrack.repositories;

import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.firebase.FirebaseManager;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    // simple in-memory auth for demo mode
    private static final Map<String, String> USERS = new HashMap<>(); // email -> password
    private static boolean signedIn = false;

    public boolean isSignedIn() {
        if (FirebaseManager.isConfigured()) {
            // real Firebase path would go here, but Firebase is disabled
            return false;
        }
        return signedIn;
    }

    public Object currentUser() {
        // Compatibility shim for existing callers that only check for null.
        // Returns a non-null placeholder when signed in, otherwise null.
        return isSignedIn() ? new Object() : null;
    }

    public void signIn(String email, String password, final ActionCallback callback) {
        if (FirebaseManager.isConfigured()) {
            callback.onError("Firebase is not enabled in this build.");
            return;
        }
        String pw = USERS.get(email);
        if (pw != null && pw.equals(password)) {
            signedIn = true;
            callback.onSuccess();
        } else {
            callback.onError("Invalid credentials (demo mode)");
        }
    }

    public void signUp(String email, String password, final ActionCallback callback) {
        if (FirebaseManager.isConfigured()) {
            callback.onError("Firebase is not enabled in this build.");
            return;
        }
        USERS.put(email, password);
        signedIn = true;
        callback.onSuccess();
    }

    public void sendPasswordReset(String email, final ActionCallback callback) {
        if (FirebaseManager.isConfigured()) {
            callback.onError("Firebase is not enabled in this build.");
            return;
        }
        if (USERS.containsKey(email)) {
            callback.onSuccess();
        } else {
            callback.onError("Email not registered (demo mode)");
        }
    }

    public void logout() {
        signedIn = false;
    }
}
