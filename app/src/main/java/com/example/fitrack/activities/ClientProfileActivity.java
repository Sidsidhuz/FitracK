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
                int[] icons = {
                    android.R.drawable.ic_menu_info_details,
                    android.R.drawable.ic_menu_edit,
                    android.R.drawable.ic_menu_sort_by_size,
                    android.R.drawable.ic_menu_today,
                    android.R.drawable.ic_menu_my_calendar,
                    android.R.drawable.ic_menu_gallery
                };
                tab.setIcon(icons[position]);
                
                String[] titles = { "Overview", "Workouts", "Analytics", "Attendance", "Schedule", "Photos" };
                tab.setContentDescription(titles[position]);
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
                
                String photoUrl = client.getProfilePhotoUrl();
                if (photoUrl != null && !photoUrl.trim().isEmpty()) {
                    try {
                        binding.clientProfileAvatar.setImageURI(android.net.Uri.parse(photoUrl));
                    } catch (Exception e) {
                        binding.clientProfileAvatar.setImageResource(android.R.drawable.sym_def_app_icon);
                    }
                } else {
                    binding.clientProfileAvatar.setImageResource(android.R.drawable.sym_def_app_icon);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
