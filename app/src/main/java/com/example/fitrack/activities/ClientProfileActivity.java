package com.example.fitrack.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.example.fitrack.adapters.ClientProfilePagerAdapter;
import com.example.fitrack.databinding.ActivityClientProfileBinding;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.viewmodels.ClientProfileViewModel;

public class ClientProfileActivity extends AppCompatActivity {
    private ActivityClientProfileBinding binding;
    private ClientProfileViewModel viewModel;
    private String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.profileToolbar);

        clientId = getIntent().getStringExtra(Constants.EXTRA_CLIENT_ID);
        String clientName = getIntent().getStringExtra(Constants.EXTRA_CLIENT_NAME);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(clientName == null ? "Client Profile" : clientName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ClientProfileViewModel();
        viewModel.loadClient(clientId);
        viewModel.loadWorkouts(clientId);
        viewModel.loadAttendance(clientId);
        viewModel.loadWeightHistory(clientId);
        viewModel.loadSchedules(clientId);
        viewModel.loadPhotos(clientId);

        binding.viewPager.setAdapter(new ClientProfilePagerAdapter(this, clientId));
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull com.google.android.material.tabs.TabLayout.Tab tab, int position) {
                String[] titles = { "Overview", "Workouts", "Analytics", "Attendance", "Schedule", "Photos" };
                tab.setText(titles[position]);
            }
        }).attach();

        viewModel.getClient().observe(this, client -> {
            if (client != null) {
                binding.clientName.setText(client.getFullName());
                binding.clientGoal.setText(client.getGoal());
                binding.clientWeight.setText(String.format("%.1f kg", client.getWeight()));
                binding.clientAttendance.setText(String.format("%.0f%%", client.getAttendancePercentage()));
                binding.clientJoinDate.setText(client.getJoinDate());
                binding.clientBmi.setText(String.format("BMI %.1f",
                        client.getHeight() > 0 ? client.getWeight() / Math.pow(client.getHeight() / 100d, 2) : 0d));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
