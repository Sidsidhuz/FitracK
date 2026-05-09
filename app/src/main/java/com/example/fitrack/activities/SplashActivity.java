package com.example.fitrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.R;
import com.example.fitrack.repositories.AuthRepository;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new AuthRepository().currentUser() == null
                    ? new Intent(SplashActivity.this, LoginActivity.class)
                    : new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }, 1200);
    }
}

