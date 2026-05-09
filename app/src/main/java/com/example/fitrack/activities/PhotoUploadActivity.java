package com.example.fitrack.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.databinding.ActivityPhotoUploadBinding;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.models.ProgressPhoto;
import com.example.fitrack.repositories.PhotoRepository;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.utils.DateUtils;

public class PhotoUploadActivity extends AppCompatActivity {
    private ActivityPhotoUploadBinding binding;
    private final PhotoRepository photoRepository = new PhotoRepository();
    private Uri selectedImageUri;
    private String clientId;
    private ActivityResultLauncher<String> imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imagePicker = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            selectedImageUri = uri;
            if (uri != null) {
                binding.selectedImagePath.setText(uri.toString());
            }
        });

        clientId = getIntent().getStringExtra(Constants.EXTRA_CLIENT_ID);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new String[] { "Before", "After", "Monthly Progress" });
        binding.photoTypeInput.setAdapter(typeAdapter);

        binding.pickImageButton.setOnClickListener(v -> imagePicker.launch("image/*"));
        binding.uploadButton.setOnClickListener(v -> uploadPhoto());
    }

    private void uploadPhoto() {
        if (clientId == null) {
            Toast.makeText(this, "Client is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressPhoto photo = new ProgressPhoto();
        photo.setClientId(clientId);
        photo.setTitle(binding.photoTitleInput.getText().toString().trim());
        photo.setCapturedOn(binding.photoDateInput.getText().toString().trim().isEmpty() ? DateUtils.today()
                : binding.photoDateInput.getText().toString().trim());
        photo.setType(binding.photoTypeInput.getText().toString().trim());
        photoRepository.uploadPhoto(this, photo, selectedImageUri, new ActionCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PhotoUploadActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(PhotoUploadActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

