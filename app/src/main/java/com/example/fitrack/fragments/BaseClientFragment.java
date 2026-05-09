package com.example.fitrack.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitrack.utils.Constants;

public abstract class BaseClientFragment extends Fragment {
    protected String clientId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            clientId = arguments.getString(Constants.EXTRA_CLIENT_ID);
        }
    }
}

