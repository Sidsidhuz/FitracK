package com.example.fitrack.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;
import com.example.fitrack.charts.FitnessChartBinder;
import com.example.fitrack.databinding.FragmentAnalyticsBinding;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsFragment extends BaseClientFragment {
    private FragmentAnalyticsBinding binding;

    public static AnalyticsFragment newInstance(String clientId) {
        AnalyticsFragment fragment = new AnalyticsFragment();
        Bundle args = new Bundle();
        args.putString(com.example.fitrack.utils.Constants.EXTRA_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1f, 60f));
        entries.add(new Entry(2f, 61.5f));
        entries.add(new Entry(3f, 63f));
        entries.add(new Entry(4f, 64f));
        FitnessChartBinder.bindLineChart(binding.analyticsChart, entries, "Strength Progress",
                Color.parseColor("#00E676"));
    }
}

