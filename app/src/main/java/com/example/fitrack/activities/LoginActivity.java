package com.example.fitrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.R;
import com.example.fitrack.databinding.ActivityLoginBinding;
import com.example.fitrack.repositories.AuthRepository;
import com.example.fitrack.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new AuthViewModel();

        if (new AuthRepository().currentUser() != null) {
            openDashboard();
            return;
        }

        binding.loginButton.setOnClickListener(v -> viewModel.login(
                binding.emailInput.getText().toString().trim(),
                binding.passwordInput.getText().toString().trim()));

        binding.signupLink.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
        binding.forgotPasswordLink
                .setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));

        viewModel.getMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (new AuthRepository().currentUser() != null) {
                    openDashboard();
                }
            }
        });
    }

    private void openDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
