package com.example.fitrack.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitrack.activities.ScheduledWorkoutActivity;
import com.example.fitrack.databinding.FragmentScheduleBinding;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.WeeklySchedule;
import com.example.fitrack.repositories.WeeklyScheduleRepository;
import com.example.fitrack.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ScheduleFragment extends BaseClientFragment {
    private FragmentScheduleBinding binding;
    private final WeeklyScheduleRepository scheduleRepository = new WeeklyScheduleRepository();
    private WeeklySchedule currentSchedule;
    private final List<Spinner> daySpinners = new ArrayList<>();

    private final String[] FOCUS_OPTIONS = {
            "Rest Day", "Chest", "Back", "Biceps", "Triceps", "Shoulders", "Legs", "Abs",
            "Chest + Triceps", "Back + Biceps", "Push Day", "Pull Day", "Full Body", "Cardio"
    };

    private final String[] DAYS_OF_WEEK = {
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
    };

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
        setupDayCards();

        binding.saveScheduleButton.setOnClickListener(v -> saveSchedule());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (clientId != null) {
            scheduleRepository.getWeeklySchedule(clientId, new DataCallback<WeeklySchedule>() {
                @Override
                public void onSuccess(WeeklySchedule data) {
                    currentSchedule = data;
                    populateSpinners();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getContext(), "Failed to load schedule", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupDayCards() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, FOCUS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (String dayName : DAYS_OF_WEEK) {
            View cardView = inflater.inflate(com.example.fitrack.R.layout.item_weekly_day_card, binding.daysContainer, false);
            TextView nameText = cardView.findViewById(com.example.fitrack.R.id.dayNameText);
            Spinner focusSpinner = cardView.findViewById(com.example.fitrack.R.id.focusSpinner);

            nameText.setText(dayName);
            focusSpinner.setAdapter(adapter);

            binding.daysContainer.addView(cardView);
            daySpinners.add(focusSpinner);
        }
    }

    private void populateSpinners() {
        if (currentSchedule == null) return;
        setSpinnerSelection(0, currentSchedule.getMondayFocus());
        setSpinnerSelection(1, currentSchedule.getTuesdayFocus());
        setSpinnerSelection(2, currentSchedule.getWednesdayFocus());
        setSpinnerSelection(3, currentSchedule.getThursdayFocus());
        setSpinnerSelection(4, currentSchedule.getFridayFocus());
        setSpinnerSelection(5, currentSchedule.getSaturdayFocus());
        setSpinnerSelection(6, currentSchedule.getSundayFocus());
    }

    private void setSpinnerSelection(int index, String focus) {
        if (focus == null) focus = "Rest Day";
        Spinner spinner = daySpinners.get(index);
        for (int i = 0; i < FOCUS_OPTIONS.length; i++) {
            if (FOCUS_OPTIONS[i].equalsIgnoreCase(focus)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveSchedule() {
        if (currentSchedule == null) {
            currentSchedule = new WeeklySchedule();
            currentSchedule.setClientId(clientId);
        }
        currentSchedule.setMondayFocus(daySpinners.get(0).getSelectedItem().toString());
        currentSchedule.setTuesdayFocus(daySpinners.get(1).getSelectedItem().toString());
        currentSchedule.setWednesdayFocus(daySpinners.get(2).getSelectedItem().toString());
        currentSchedule.setThursdayFocus(daySpinners.get(3).getSelectedItem().toString());
        currentSchedule.setFridayFocus(daySpinners.get(4).getSelectedItem().toString());
        currentSchedule.setSaturdayFocus(daySpinners.get(5).getSelectedItem().toString());
        currentSchedule.setSundayFocus(daySpinners.get(6).getSelectedItem().toString());

        scheduleRepository.saveWeeklySchedule(currentSchedule, new ActionCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Schedule saved!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
