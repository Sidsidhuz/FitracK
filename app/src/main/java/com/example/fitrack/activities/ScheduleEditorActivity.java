package com.example.fitrack.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.databinding.ActivityScheduleEditorBinding;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.models.Schedule;
import com.example.fitrack.repositories.ScheduleRepository;
import com.example.fitrack.utils.Constants;

public class ScheduleEditorActivity extends AppCompatActivity {
    private ActivityScheduleEditorBinding binding;
    private final ScheduleRepository scheduleRepository = new ScheduleRepository();
    private String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clientId = getIntent().getStringExtra(Constants.EXTRA_CLIENT_ID);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, com.example.fitrack.R.array.week_days,
                android.R.layout.simple_list_item_1);
        binding.dayInput.setAdapter(dayAdapter);

        binding.saveScheduleButton.setOnClickListener(v -> saveSchedule());
    }

    private void saveSchedule() {
        if (clientId == null) {
            Toast.makeText(this, "Client is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        Schedule schedule = new Schedule();
        schedule.setClientId(clientId);
        schedule.setDay(binding.dayInput.getText().toString().trim());
        schedule.setFocus(binding.focusInput.getText().toString().trim());
        schedule.setNotes(binding.notesInput.getText().toString().trim());
        scheduleRepository.saveSchedule(schedule, new ActionCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(ScheduleEditorActivity.this, "Schedule saved", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ScheduleEditorActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

