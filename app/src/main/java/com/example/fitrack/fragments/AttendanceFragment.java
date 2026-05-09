package com.example.fitrack.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitrack.adapters.AttendanceAdapter;
import com.example.fitrack.databinding.FragmentAttendanceBinding;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Attendance;
import com.example.fitrack.repositories.AttendanceRepository;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.utils.DateUtils;

import java.util.List;

public class AttendanceFragment extends BaseClientFragment {
    private FragmentAttendanceBinding binding;
    private final AttendanceRepository attendanceRepository = new AttendanceRepository();
    private final AttendanceAdapter adapter = new AttendanceAdapter();

    public static AttendanceFragment newInstance(String clientId) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.attendanceRecyclerView.setAdapter(adapter);

        binding.presentButton.setOnClickListener(v -> markAttendance(true));
        binding.absentButton.setOnClickListener(v -> markAttendance(false));

        if (clientId != null) {
            attendanceRepository.getAttendanceForClient(clientId, new DataCallback<List<Attendance>>() {
                @Override
                public void onSuccess(List<Attendance> data) {
                    adapter.submitList(data);
                    binding.attendanceEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onError(String message) {
                    binding.attendanceEmpty.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void markAttendance(boolean present) {
        if (clientId == null) {
            Toast.makeText(requireContext(), "Select a client first", Toast.LENGTH_SHORT).show();
            return;
        }
        Attendance attendance = new Attendance();
        attendance.setClientId(clientId);
        attendance.setDate(DateUtils.today());
        attendance.setPresent(present);
        attendance.setNotes(present ? "Workout completed" : "Missed session");
        attendanceRepository.markAttendance(attendance, new ActionCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(requireContext(), "Attendance saved", Toast.LENGTH_SHORT).show();
                attendanceRepository.getAttendanceForClient(clientId, new DataCallback<List<Attendance>>() {
                    @Override
                    public void onSuccess(List<Attendance> data) {
                        adapter.submitList(data);
                    }

                    @Override
                    public void onError(String message) {
                    }
                });
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

