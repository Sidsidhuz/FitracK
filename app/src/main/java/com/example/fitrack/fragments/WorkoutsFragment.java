package com.example.fitrack.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitrack.R;
import com.example.fitrack.activities.ScheduledWorkoutActivity;
import com.example.fitrack.adapters.WorkoutAdapter;
import com.example.fitrack.databinding.FragmentWorkoutsBinding;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.WeeklySchedule;
import com.example.fitrack.models.WorkoutSession;
import com.example.fitrack.repositories.WeeklyScheduleRepository;
import com.example.fitrack.repositories.WorkoutSessionRepository;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

public class WorkoutsFragment extends BaseClientFragment {
    private FragmentWorkoutsBinding binding;
    private final WorkoutSessionRepository sessionRepository = new WorkoutSessionRepository();
    private final WeeklyScheduleRepository scheduleRepository = new WeeklyScheduleRepository();
    private final WorkoutAdapter adapter = new WorkoutAdapter();
    private WeeklySchedule currentSchedule;
    private WorkoutSession todaySession;

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

        binding.startWorkoutButton.setOnClickListener(v -> startTodayWorkout());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSessions();
        loadSchedule();
    }

    private void loadSessions() {
        if (clientId != null) {
            sessionRepository.getWorkoutSessionsForClient(clientId, new DataCallback<List<WorkoutSession>>() {
                @Override
                public void onSuccess(List<WorkoutSession> data) {
                    adapter.submitList(data);
                    binding.workoutEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                    
                    String todayStr = DateUtils.today();
                    todaySession = null;
                    for (WorkoutSession session : data) {
                        if (todayStr.equals(session.getDate())) {
                            todaySession = session;
                            break;
                        }
                    }
                    updateTodayWorkoutButton();
                }

                @Override
                public void onError(String message) {
                    binding.workoutEmpty.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void loadSchedule() {
        if (clientId != null) {
            scheduleRepository.getWeeklySchedule(clientId, new DataCallback<WeeklySchedule>() {
                @Override
                public void onSuccess(WeeklySchedule data) {
                    currentSchedule = data;
                    updateTodayWorkoutButton();
                }

                @Override
                public void onError(String message) {
                    // Ignore, maybe no schedule yet
                }
            });
        }
    }

    private void updateTodayWorkoutButton() {
        if (currentSchedule == null) return;

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String todayFocus = "Rest Day";

        switch (dayOfWeek) {
            case Calendar.MONDAY: todayFocus = currentSchedule.getMondayFocus(); break;
            case Calendar.TUESDAY: todayFocus = currentSchedule.getTuesdayFocus(); break;
            case Calendar.WEDNESDAY: todayFocus = currentSchedule.getWednesdayFocus(); break;
            case Calendar.THURSDAY: todayFocus = currentSchedule.getThursdayFocus(); break;
            case Calendar.FRIDAY: todayFocus = currentSchedule.getFridayFocus(); break;
            case Calendar.SATURDAY: todayFocus = currentSchedule.getSaturdayFocus(); break;
            case Calendar.SUNDAY: todayFocus = currentSchedule.getSundayFocus(); break;
        }

        if (todayFocus == null || todayFocus.equalsIgnoreCase("Rest Day")) {
            binding.startWorkoutButton.setText("TODAY IS A REST DAY");
            binding.startWorkoutButton.setEnabled(false);
            binding.startWorkoutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#424242")));
            binding.startWorkoutButton.setTextColor(Color.parseColor("#A0AAB2"));
        } else {
            if (todaySession != null) {
                if (todaySession.isCompleted()) {
                    binding.startWorkoutButton.setText("EDIT COMPLETED: " + todayFocus.toUpperCase());
                } else {
                    binding.startWorkoutButton.setText("CONTINUE SESSION: " + todayFocus.toUpperCase());
                }
            } else {
                binding.startWorkoutButton.setText("RECORD TODAY: " + todayFocus.toUpperCase());
            }
            binding.startWorkoutButton.setEnabled(true);
            binding.startWorkoutButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.fitness_primary)));
            binding.startWorkoutButton.setTextColor(Color.BLACK);
        }
    }

    private void startTodayWorkout() {
        if (currentSchedule == null) {
            Toast.makeText(getContext(), "Please create a schedule in the Schedule tab first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String todayFocus = "Rest Day";

        switch (dayOfWeek) {
            case Calendar.MONDAY: todayFocus = currentSchedule.getMondayFocus(); break;
            case Calendar.TUESDAY: todayFocus = currentSchedule.getTuesdayFocus(); break;
            case Calendar.WEDNESDAY: todayFocus = currentSchedule.getWednesdayFocus(); break;
            case Calendar.THURSDAY: todayFocus = currentSchedule.getThursdayFocus(); break;
            case Calendar.FRIDAY: todayFocus = currentSchedule.getFridayFocus(); break;
            case Calendar.SATURDAY: todayFocus = currentSchedule.getSaturdayFocus(); break;
            case Calendar.SUNDAY: todayFocus = currentSchedule.getSundayFocus(); break;
        }

        if (todayFocus == null || todayFocus.equalsIgnoreCase("Rest Day")) {
            Toast.makeText(getContext(), "Today is a Rest Day!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(requireContext(), ScheduledWorkoutActivity.class);
        intent.putExtra(Constants.EXTRA_CLIENT_ID, clientId);
        intent.putExtra("EXTRA_FOCUS", todayFocus);
        if (todaySession != null) {
            intent.putExtra("EXTRA_SESSION_ID", todaySession.getId());
        }
        startActivity(intent);
    }
}
