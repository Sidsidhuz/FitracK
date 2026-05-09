package com.example.fitrack.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.databinding.ActivityForgotPasswordBinding;
import com.example.fitrack.viewmodels.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new AuthViewModel();

        binding.resetButton
                .setOnClickListener(v -> viewModel.resetPassword(binding.emailInput.getText().toString().trim()));

        viewModel.getMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
