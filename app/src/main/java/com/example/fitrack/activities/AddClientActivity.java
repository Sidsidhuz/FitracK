package com.example.fitrack.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.databinding.ActivityAddClientBinding;
import com.example.fitrack.models.Client;
import com.example.fitrack.repositories.ClientRepository;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.utils.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class AddClientActivity extends AppCompatActivity {
    private ActivityAddClientBinding binding;
    private final ClientRepository clientRepository = new ClientRepository();
    private Client editingClient;
    private String selectedPhotoPath = "";

    private final ActivityResultLauncher<Intent> photoPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        saveImageToInternalStorage(selectedImageUri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                Constants.CLIENT_GOALS);
        binding.goalInput.setAdapter(goalAdapter);

        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genders);
        binding.genderInput.setAdapter(genderAdapter);

        binding.joinDateInput.setOnClickListener(v -> showDatePicker());
        
        binding.selectPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            photoPickerLauncher.launch(intent);
        });

        editingClient = (Client) getIntent().getSerializableExtra("client");
        if (editingClient != null) {
            prefill(editingClient);
            binding.saveClientButton.setText("Update Client");
        }

        binding.saveClientButton.setOnClickListener(v -> saveClient());
    }

    private void saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                String fileName = "profile_" + UUID.randomUUID().toString() + ".jpg";
                File file = new File(getFilesDir(), fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                
                outputStream.close();
                inputStream.close();
                
                selectedPhotoPath = file.getAbsolutePath();
                binding.profilePhotoPreview.setImageURI(Uri.parse(selectedPhotoPath));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void prefill(Client client) {
        binding.fullNameInput.setText(client.getFullName());
        binding.ageInput.setText(String.valueOf(client.getAge()));
        binding.genderInput.setText(client.getGender(), false);
        binding.heightInput.setText(String.valueOf(client.getHeight()));
        binding.weightInput.setText(String.valueOf(client.getWeight()));
        binding.goalInput.setText(client.getGoal(), false);
        binding.phoneInput.setText(client.getPhoneNumber());
        binding.joinDateInput.setText(client.getJoinDate());
        
        selectedPhotoPath = client.getProfilePhotoUrl();
        if (selectedPhotoPath != null && !selectedPhotoPath.trim().isEmpty()) {
            binding.profilePhotoPreview.setImageURI(Uri.parse(selectedPhotoPath));
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.US, "%02d/%02d/%04d", month + 1, dayOfMonth, year);
            binding.joinDateInput.setText(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
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
        client.setProfilePhotoUrl(selectedPhotoPath);

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
        try { return Integer.parseInt(value); } catch (Exception ignored) { return 0; }
    }

    private double parseDouble(String value) {
        try { return Double.parseDouble(value); } catch (Exception ignored) { return 0d; }
    }
}

