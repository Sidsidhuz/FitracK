package com.example.fitrack.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitrack.databinding.FragmentOverviewBinding;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.WeeklySchedule;
import com.example.fitrack.models.WorkoutExercise;
import com.example.fitrack.models.WorkoutSession;
import com.example.fitrack.repositories.WeeklyScheduleRepository;
import com.example.fitrack.repositories.WorkoutSessionRepository;
import com.example.fitrack.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OverviewFragment extends BaseClientFragment {
    private FragmentOverviewBinding binding;
    private final WeeklyScheduleRepository scheduleRepository = new WeeklyScheduleRepository();
    private final WorkoutSessionRepository sessionRepository = new WorkoutSessionRepository();

    private WeeklySchedule currentSchedule;
    private List<WorkoutSession> allSessions = new ArrayList<>();
    private String todayFocus = "Rest Day";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("MM/dd", Locale.US);

    public static OverviewFragment newInstance(String clientId) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (clientId == null) return;

        scheduleRepository.getWeeklySchedule(clientId, new DataCallback<WeeklySchedule>() {
            @Override
            public void onSuccess(WeeklySchedule data) {
                currentSchedule = data;
                determineTodayFocus();
            }

            @Override
            public void onError(String message) {
                determineTodayFocus();
            }
        });

        sessionRepository.getWorkoutSessionsForClient(clientId, new DataCallback<List<WorkoutSession>>() {
            @Override
            public void onSuccess(List<WorkoutSession> data) {
                allSessions = data;
                processCharts();
            }

            @Override
            public void onError(String message) {
                allSessions = new ArrayList<>();
                processCharts();
            }
        });
    }

    private void determineTodayFocus() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        todayFocus = "Rest Day";

        if (currentSchedule != null) {
            switch (dayOfWeek) {
                case Calendar.MONDAY: todayFocus = currentSchedule.getMondayFocus(); break;
                case Calendar.TUESDAY: todayFocus = currentSchedule.getTuesdayFocus(); break;
                case Calendar.WEDNESDAY: todayFocus = currentSchedule.getWednesdayFocus(); break;
                case Calendar.THURSDAY: todayFocus = currentSchedule.getThursdayFocus(); break;
                case Calendar.FRIDAY: todayFocus = currentSchedule.getFridayFocus(); break;
                case Calendar.SATURDAY: todayFocus = currentSchedule.getSaturdayFocus(); break;
                case Calendar.SUNDAY: todayFocus = currentSchedule.getSundayFocus(); break;
            }
            if (todayFocus == null) todayFocus = "Rest Day";
        }

        binding.todayFocusText.setText(todayFocus.toUpperCase());
        processCharts();
    }

    private void processCharts() {
        if (allSessions == null || todayFocus == null) return;

        // Sort all sessions by date ascending
        Collections.sort(allSessions, new Comparator<WorkoutSession>() {
            @Override
            public int compare(WorkoutSession s1, WorkoutSession s2) {
                try {
                    Date d1 = dateFormat.parse(s1.getDate());
                    Date d2 = dateFormat.parse(s2.getDate());
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    return 0;
                }
            }
        });

        processComparisonChart();
        processTrendChart();
    }

    private void processComparisonChart() {
        if (todayFocus.equalsIgnoreCase("Rest Day")) {
            binding.comparisonChart.setVisibility(View.GONE);
            binding.comparisonEmptyText.setVisibility(View.VISIBLE);
            binding.comparisonEmptyText.setText("Today is a Rest Day.\nEnjoy your recovery!");
            return;
        }

        List<WorkoutSession> matchingSessions = new ArrayList<>();
        for (WorkoutSession session : allSessions) {
            if (todayFocus.equalsIgnoreCase(session.getFocus()) && session.isCompleted()) {
                matchingSessions.add(session);
            }
        }

        if (matchingSessions.isEmpty()) {
            binding.comparisonChart.setVisibility(View.GONE);
            binding.comparisonEmptyText.setVisibility(View.VISIBLE);
            binding.comparisonEmptyText.setText("No previous data found for " + todayFocus + ".\nSet this as your starting point!");
            return;
        }

        binding.comparisonEmptyText.setVisibility(View.GONE);
        binding.comparisonChart.setVisibility(View.VISIBLE);

        List<Float> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (WorkoutSession session : matchingSessions) {
            values.add((float) calculateVolume(session));
            try {
                Date d = dateFormat.parse(session.getDate());
                labels.add(shortDateFormat.format(d));
            } catch (Exception e) {
                labels.add("");
            }
        }

        binding.comparisonChart.setData(values, labels);
    }

    private void processTrendChart() {
        if (allSessions.isEmpty()) {
            binding.trendChart.setData(new ArrayList<>(), new ArrayList<>());
            return;
        }

        List<WorkoutSession> completedSessions = new ArrayList<>();
        for (WorkoutSession s : allSessions) {
            if (s.isCompleted()) completedSessions.add(s);
        }

        // Take last 5 sessions
        int start = Math.max(0, completedSessions.size() - 5);
        List<WorkoutSession> recentSessions = completedSessions.subList(start, completedSessions.size());

        List<Float> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (WorkoutSession session : recentSessions) {
            values.add((float) calculateVolume(session));
            try {
                Date d = dateFormat.parse(session.getDate());
                labels.add(shortDateFormat.format(d));
            } catch (Exception e) {
                labels.add("");
            }
        }

        binding.trendChart.setData(values, labels);
    }

    private double calculateVolume(WorkoutSession session) {
        if (session.getExercises() == null) return 0;
        double volume = 0;
        for (WorkoutExercise ex : session.getExercises()) {
            volume += (ex.getSets() * ex.getReps() * ex.getWeight());
        }
        return volume;
    }
}
