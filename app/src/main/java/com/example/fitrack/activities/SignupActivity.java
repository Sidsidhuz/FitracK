package com.example.fitrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.databinding.ActivitySignupBinding;
import com.example.fitrack.viewmodels.AuthViewModel;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new AuthViewModel();

        binding.createAccountButton.setOnClickListener(v -> viewModel.signup(
                binding.emailInput.getText().toString().trim(),
                binding.passwordInput.getText().toString().trim()));

        binding.loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        viewModel.getMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (message.toLowerCase().contains("account created")) {
                    startActivity(new Intent(this, DashboardActivity.class));
                    finish();
                }
            }
        });
    }
}
