package com.example.fitrack.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitrack.activities.WorkoutEntryActivity;
import com.example.fitrack.adapters.WorkoutAdapter;
import com.example.fitrack.databinding.FragmentWorkoutsBinding;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Workout;
import com.example.fitrack.repositories.WorkoutRepository;
import com.example.fitrack.utils.Constants;

import java.util.List;

public class WorkoutsFragment extends BaseClientFragment {
    private FragmentWorkoutsBinding binding;
    private final WorkoutRepository workoutRepository = new WorkoutRepository();
    private final WorkoutAdapter adapter = new WorkoutAdapter();

    public static WorkoutsFragment newInstance(String clientId) {
        WorkoutsFragment fragment = new WorkoutsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.workoutRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.workoutRecyclerView.setAdapter(adapter);
        binding.addWorkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), WorkoutEntryActivity.class);
            intent.putExtra(Constants.EXTRA_CLIENT_ID, clientId);
            startActivity(intent);
        });
        if (clientId != null) {
            workoutRepository.getWorkoutsForClient(clientId, new DataCallback<List<Workout>>() {
                @Override
                public void onSuccess(List<Workout> data) {
                    adapter.submitList(data);
                    binding.workoutEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onError(String message) {
                    binding.workoutEmpty.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}

