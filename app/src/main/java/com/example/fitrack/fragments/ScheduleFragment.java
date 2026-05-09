package com.example.fitrack.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitrack.activities.ScheduleEditorActivity;
import com.example.fitrack.adapters.ScheduleAdapter;
import com.example.fitrack.databinding.FragmentScheduleBinding;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Schedule;
import com.example.fitrack.repositories.ScheduleRepository;
import com.example.fitrack.utils.Constants;

import java.util.List;

public class ScheduleFragment extends BaseClientFragment {
    private FragmentScheduleBinding binding;
    private final ScheduleRepository scheduleRepository = new ScheduleRepository();
    private final ScheduleAdapter adapter = new ScheduleAdapter();

    public static ScheduleFragment newInstance(String clientId) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.scheduleRecyclerView.setAdapter(adapter);
        binding.editScheduleButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ScheduleEditorActivity.class);
            intent.putExtra(Constants.EXTRA_CLIENT_ID, clientId);
            startActivity(intent);
        });
        if (clientId != null) {
            scheduleRepository.getSchedules(clientId, new DataCallback<List<Schedule>>() {
                @Override
                public void onSuccess(List<Schedule> data) {
                    adapter.submitList(data);
                    binding.scheduleEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onError(String message) {
                    binding.scheduleEmpty.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}

