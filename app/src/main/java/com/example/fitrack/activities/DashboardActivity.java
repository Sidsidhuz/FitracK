package com.example.fitrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.example.fitrack.R;
import com.example.fitrack.repositories.AuthRepository;
import com.example.fitrack.fragments.AnalyticsFragment;
import com.example.fitrack.fragments.AttendanceFragment;
import com.example.fitrack.fragments.DashboardFragment;
import com.example.fitrack.fragments.ProgressPhotosFragment;
import com.example.fitrack.fragments.ScheduleFragment;

public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private final AuthRepository authRepository = new AuthRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.dashboardToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        NavigationView navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.dashboard_title);
        }

        if (savedInstanceState == null) {
            showFragment(new DashboardFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                showFragment(new DashboardFragment());
                return true;
            }
            if (itemId == R.id.menu_analytics) {
                showFragment(AnalyticsFragment.newInstance(null));
                return true;
            }
            if (itemId == R.id.menu_schedule) {
                showFragment(ScheduleFragment.newInstance(null));
                return true;
            }
            if (itemId == R.id.menu_attendance) {
                showFragment(AttendanceFragment.newInstance(null));
                return true;
            }
            if (itemId == R.id.menu_photos) {
                showFragment(ProgressPhotosFragment.newInstance(null));
                return true;
            }
            return false;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.drawer_add_client) {
                startActivity(new Intent(this, AddClientActivity.class));
            } else if (item.getItemId() == R.id.drawer_logout) {
                authRepository.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dashboardContainer, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
