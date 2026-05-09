package com.example.fitrack.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitrack.fragments.AnalyticsFragment;
import com.example.fitrack.fragments.AttendanceFragment;
import com.example.fitrack.fragments.OverviewFragment;
import com.example.fitrack.fragments.ProgressPhotosFragment;
import com.example.fitrack.fragments.ScheduleFragment;
import com.example.fitrack.fragments.WorkoutsFragment;
import com.example.fitrack.utils.Constants;

public class ClientProfilePagerAdapter extends FragmentStateAdapter {
    private final String clientId;

    public ClientProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity, String clientId) {
        super(fragmentActivity);
        this.clientId = clientId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = WorkoutsFragment.newInstance(clientId);
                break;
            case 2:
                fragment = AnalyticsFragment.newInstance(clientId);
                break;
            case 3:
                fragment = AttendanceFragment.newInstance(clientId);
                break;
            case 4:
                fragment = ScheduleFragment.newInstance(clientId);
                break;
            case 5:
                fragment = ProgressPhotosFragment.newInstance(clientId);
                break;
            case 0:
            default:
                fragment = OverviewFragment.newInstance(clientId);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}

