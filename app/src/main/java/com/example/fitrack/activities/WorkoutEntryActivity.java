package com.example.fitrack.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrack.databinding.ActivityWorkoutEntryBinding;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Workout;
import com.example.fitrack.repositories.WorkoutRepository;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.utils.DateUtils;
import com.example.fitrack.utils.PersonalRecordDetector;
import com.example.fitrack.utils.WorkoutCatalog;

import java.util.List;

public class WorkoutEntryActivity extends AppCompatActivity {
    private ActivityWorkoutEntryBinding binding;
    private final WorkoutRepository workoutRepository = new WorkoutRepository();
    private String clientId;
    private String selectedCategory = "Chest";
    private String selectedExercise = "Bench Press";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clientId = getIntent().getStringExtra(Constants.EXTRA_CLIENT_ID);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                WorkoutCatalog.getCategories());
        binding.categoryInput.setAdapter(categoryAdapter);
        binding.categoryInput.setText(selectedCategory, false);
        bindExercises(selectedCategory);

        binding.categoryInput.setOnItemClickListener((parent, view, position, id) -> {
            selectedCategory = categoryAdapter.getItem(position);
            bindExercises(selectedCategory);
        });

        binding.exerciseInput.setOnItemClickListener((parent, view, position,
                id) -> selectedExercise = binding.exerciseInput.getAdapter().getItem(position).toString());

        binding.saveWorkoutButton.setOnClickListener(v -> saveWorkout());
    }

    private void bindExercises(String category) {
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                WorkoutCatalog.getExercises(category));
        binding.exerciseInput.setAdapter(exerciseAdapter);
        if (exerciseAdapter.getCount() > 0) {
            selectedExercise = exerciseAdapter.getItem(0);
            binding.exerciseInput.setText(selectedExercise, false);
        }
    }

    private void saveWorkout() {
        if (clientId == null) {
            Toast.makeText(this, "Client is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        final Workout workout = new Workout();
        workout.setClientId(clientId);
        workout.setCategory(binding.categoryInput.getText().toString().trim());
        workout.setExerciseName(binding.exerciseInput.getText().toString().trim());
        workout.setDate(binding.dateInput.getText().toString().trim().isEmpty() ? DateUtils.today()
                : binding.dateInput.getText().toString().trim());
        workout.setWeightUsed(parseDouble(binding.weightInput.getText().toString().trim()));
        workout.setSets(parseInt(binding.setsInput.getText().toString().trim()));
        workout.setReps(parseInt(binding.repsInput.getText().toString().trim()));
        workout.setNotes(binding.notesInput.getText().toString().trim());

        workoutRepository.getWorkoutsForClient(clientId, new DataCallback<List<Workout>>() {
            @Override
            public void onSuccess(List<Workout> data) {
                double previousMax = 0d;
                for (Workout item : data) {
                    if (workout.getExerciseName().equalsIgnoreCase(item.getExerciseName())
                            && item.getWeightUsed() > previousMax) {
                        previousMax = item.getWeightUsed();
                    }
                }
                workout.setPersonalRecord(PersonalRecordDetector.isNewRecord(workout.getWeightUsed(), previousMax));
                workoutRepository.saveWorkout(workout, new ActionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(WorkoutEntryActivity.this, "Workout saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(WorkoutEntryActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String message) {
                Toast.makeText(WorkoutEntryActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {
            return 0d;
        }
    }
}

