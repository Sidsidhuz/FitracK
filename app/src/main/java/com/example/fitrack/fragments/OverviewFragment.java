package com.example.fitrack.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitrack.databinding.FragmentOverviewBinding;
import com.example.fitrack.models.Client;

public class OverviewFragment extends BaseClientFragment {
    private FragmentOverviewBinding binding;

    public static OverviewFragment newInstance(String clientId) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(com.example.fitrack.utils.Constants.EXTRA_CLIENT_ID, clientId);
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
        binding.overviewEmpty
                .setText(clientId == null ? "Overall dashboard overview" : "Client overview loading from Firestore...");
    }
}

