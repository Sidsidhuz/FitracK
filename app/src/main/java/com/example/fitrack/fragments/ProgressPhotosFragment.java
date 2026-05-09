package com.example.fitrack.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitrack.activities.PhotoUploadActivity;
import com.example.fitrack.adapters.PhotoAdapter;
import com.example.fitrack.databinding.FragmentProgressPhotosBinding;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.ProgressPhoto;
import com.example.fitrack.repositories.PhotoRepository;
import com.example.fitrack.utils.Constants;

import java.util.List;

public class ProgressPhotosFragment extends BaseClientFragment {
    private FragmentProgressPhotosBinding binding;
    private final PhotoRepository photoRepository = new PhotoRepository();
    private final PhotoAdapter photoAdapter = new PhotoAdapter();

    public static ProgressPhotosFragment newInstance(String clientId) {
        ProgressPhotosFragment fragment = new ProgressPhotosFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentProgressPhotosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.photoListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.photoListRecyclerView.setAdapter(photoAdapter);
        binding.uploadPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PhotoUploadActivity.class);
            intent.putExtra(Constants.EXTRA_CLIENT_ID, clientId);
            startActivity(intent);
        });
        if (clientId != null) {
            photoRepository.getPhotos(clientId, new DataCallback<List<ProgressPhoto>>() {
                @Override
                public void onSuccess(List<ProgressPhoto> data) {
                    photoAdapter.submitList(data);
                    binding.progressPhotosEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onError(String message) {
                    binding.progressPhotosEmpty.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}

