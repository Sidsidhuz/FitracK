package com.example.fitrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.databinding.ActivityAddClientBinding;
import com.example.fitrack.models.Client;
import com.example.fitrack.repositories.ClientRepository;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.utils.DateUtils;

public class AddClientActivity extends AppCompatActivity {
    private ActivityAddClientBinding binding;
    private final ClientRepository clientRepository = new ClientRepository();
    private Client editingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                Constants.CLIENT_GOALS);
        binding.goalInput.setAdapter(goalAdapter);

        editingClient = (Client) getIntent().getSerializableExtra("client");
        if (editingClient != null) {
            prefill(editingClient);
            binding.saveClientButton.setText("Update Client");
        }

        binding.saveClientButton.setOnClickListener(v -> saveClient());
    }

    private void prefill(Client client) {
        binding.fullNameInput.setText(client.getFullName());
        binding.ageInput.setText(String.valueOf(client.getAge()));
        binding.genderInput.setText(client.getGender());
        binding.heightInput.setText(String.valueOf(client.getHeight()));
        binding.weightInput.setText(String.valueOf(client.getWeight()));
        binding.goalInput.setText(client.getGoal());
        binding.phoneInput.setText(client.getPhoneNumber());
        binding.joinDateInput.setText(client.getJoinDate());
        binding.photoUrlInput.setText(client.getProfilePhotoUrl());
    }

    private void saveClient() {
        Client client = editingClient == null ? new Client() : editingClient;
        client.setFullName(binding.fullNameInput.getText().toString().trim());
        client.setAge(parseInt(binding.ageInput.getText().toString().trim()));
        client.setGender(binding.genderInput.getText().toString().trim());
        client.setHeight(parseDouble(binding.heightInput.getText().toString().trim()));
        client.setWeight(parseDouble(binding.weightInput.getText().toString().trim()));
        client.setGoal(binding.goalInput.getText().toString().trim());
        client.setPhoneNumber(binding.phoneInput.getText().toString().trim());
        client.setJoinDate(binding.joinDateInput.getText().toString().trim().isEmpty() ? DateUtils.today()
                : binding.joinDateInput.getText().toString().trim());
        client.setProfilePhotoUrl(binding.photoUrlInput.getText().toString().trim());

        if (editingClient == null) {
            clientRepository.saveClient(client, new com.example.fitrack.interfaces.ActionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddClientActivity.this, "Client saved", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(AddClientActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            clientRepository.updateClient(client, new com.example.fitrack.interfaces.ActionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddClientActivity.this, "Client updated", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(AddClientActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {
            return 0d;
        }
    }
}

