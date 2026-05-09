package com.example.fitrack.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.fitrack.GymTrackerApp;
import com.example.fitrack.interfaces.ActionCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private static final String PREFS_NAME = "fitrack_auth";
    private static final String KEY_USERS = "users";
    private static final String KEY_SIGNED_IN = "signed_in";
    private static final String KEY_SESSION_EMAIL = "session_email";

    public boolean isSignedIn() {
        return preferences().getBoolean(KEY_SIGNED_IN, false);
    }

    public Object currentUser() {
        String email = preferences().getString(KEY_SESSION_EMAIL, null);
        return isSignedIn() ? email : null;
    }

    public void signIn(String email, String password, final ActionCallback callback) {
        String pw = loadUsers().get(email);
        if (pw != null && pw.equals(password)) {
            preferences().edit()
                    .putBoolean(KEY_SIGNED_IN, true)
                    .putString(KEY_SESSION_EMAIL, email)
                    .apply();
            callback.onSuccess();
        } else {
            callback.onError("Invalid credentials");
        }
    }

    public void signUp(String email, String password, final ActionCallback callback) {
        Map<String, String> users = loadUsers();
        users.put(email, password);
        saveUsers(users);
        preferences().edit()
                .putBoolean(KEY_SIGNED_IN, true)
                .putString(KEY_SESSION_EMAIL, email)
                .apply();
        callback.onSuccess();
    }

    public void sendPasswordReset(String email, final ActionCallback callback) {
        if (loadUsers().containsKey(email)) {
            callback.onSuccess();
        } else {
            callback.onError("Email not registered");
        }
    }

    public void logout() {
        preferences().edit()
                .putBoolean(KEY_SIGNED_IN, false)
                .remove(KEY_SESSION_EMAIL)
                .apply();
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> loadUsers() {
        String encodedUsers = preferences().getString(KEY_USERS, null);
        if (encodedUsers == null || encodedUsers.isEmpty()) {
            return new HashMap<>();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new ByteArrayInputStream(Base64.decode(encodedUsers, Base64.DEFAULT)))) {
            Object stored = inputStream.readObject();
            if (stored instanceof Map) {
                return new HashMap<>((Map<String, String>) stored);
            }
        } catch (IOException | ClassNotFoundException ignored) {
            // Fall through to empty auth store if the data is unreadable.
        }
        return new HashMap<>();
    }

    private void saveUsers(Map<String, String> users) {
        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
            objectOutputStream.writeObject((Serializable) new HashMap<>(users));
            objectOutputStream.flush();
            String encodedUsers = Base64.encodeToString(byteOutputStream.toByteArray(), Base64.NO_WRAP);
            preferences().edit().putString(KEY_USERS, encodedUsers).apply();
        } catch (IOException ignored) {
            // Best-effort persistence for demo mode.
        }
    }

    private SharedPreferences preferences() {
        Context context = GymTrackerApp.getInstance();
        if (context == null) {
            throw new IllegalStateException("Application context is not available yet");
        }
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
