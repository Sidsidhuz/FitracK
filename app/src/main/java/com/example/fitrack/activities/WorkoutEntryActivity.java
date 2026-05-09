package com.example.fitrack.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.example.fitrack.utils.ExerciseLibrary;
import com.example.fitrack.utils.PersonalRecordDetector;
import com.example.fitrack.utils.WorkoutCategoryMapper;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorkoutEntryActivity extends AppCompatActivity {
    private ActivityWorkoutEntryBinding binding;
    private final WorkoutRepository workoutRepository = new WorkoutRepository();
    private String clientId;
    private String selectedCategory = WorkoutCategoryMapper.normalize("Chest");
    private String selectedExercise = ExerciseLibrary.defaultExerciseFor("Chest");
    private String selectedDate = DateUtils.today();
    private boolean saving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clientId = getIntent().getStringExtra(Constants.EXTRA_CLIENT_ID);

        setupCategoryDropdown();
        setupExerciseDropdown(selectedCategory);
        setupDateField();
        updatePreview();
        updateSaveState(false);

        binding.categoryInput.setOnItemClickListener((parent, view, position, id) -> {
            selectedCategory = WorkoutCategoryMapper
                    .normalize(binding.categoryInput.getAdapter().getItem(position).toString());
            setupExerciseDropdown(selectedCategory);
            updatePreview();
        });

        binding.exerciseInput.setOnItemClickListener((parent, view, position, id) -> {
            selectedExercise = binding.exerciseInput.getAdapter().getItem(position).toString();
            updatePreview();
        });

        binding.categoryInput.setOnClickListener(v -> binding.categoryInput.showDropDown());
        binding.exerciseInput.setOnClickListener(v -> binding.exerciseInput.showDropDown());
        binding.dateInput.setOnClickListener(v -> showDatePicker());
        binding.saveWorkoutButton.setOnClickListener(v -> saveWorkout());
    }

    private void setupCategoryDropdown() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                ExerciseLibrary.categories());
        binding.categoryInput.setAdapter(categoryAdapter);
        binding.categoryInput.setText(selectedCategory, false);
    }

    private void setupExerciseDropdown(String category) {
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                ExerciseLibrary.exercisesFor(category));
        binding.exerciseInput.setAdapter(exerciseAdapter);
        if (exerciseAdapter.getCount() > 0) {
            selectedExercise = exerciseAdapter.getItem(0);
            binding.exerciseInput.setText(selectedExercise, false);
        } else {
            selectedExercise = "";
            binding.exerciseInput.setText("", false);
        }
        updatePreview();
    }

    private void setupDateField() {
        selectedDate = DateUtils.today();
        binding.dateInput.setText(selectedDate);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            binding.dateInput.setText(selectedDate);
            updatePreview();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updatePreview() {
        if (binding == null) {
            return;
        }
        String category = binding.categoryInput.getText() == null ? selectedCategory
                : binding.categoryInput.getText().toString().trim();
        String exercise = binding.exerciseInput.getText() == null || TextUtils.isEmpty(binding.exerciseInput.getText())
                ? selectedExercise
                : binding.exerciseInput.getText().toString().trim();
        String date = binding.dateInput.getText() == null || TextUtils.isEmpty(binding.dateInput.getText())
                ? selectedDate
                : binding.dateInput.getText().toString().trim();
        String weight = binding.weightInput.getText() == null || TextUtils.isEmpty(binding.weightInput.getText())
                ? "0"
                : binding.weightInput.getText().toString().trim();

        binding.previewCategoryValue.setText(category);
        binding.previewExerciseValue.setText(exercise);
        binding.previewDateValue.setText(date);
        binding.previewVolumeValue.setText(weight + " kg ready");
    }

    private void saveWorkout() {
        if (saving) {
            return;
        }
        if (clientId == null || clientId.trim().isEmpty()) {
            Toast.makeText(this, "Client is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = binding.categoryInput.getText() == null ? ""
                : binding.categoryInput.getText().toString().trim();
        String exercise = binding.exerciseInput.getText() == null ? ""
                : binding.exerciseInput.getText().toString().trim();
        String date = binding.dateInput.getText() == null ? "" : binding.dateInput.getText().toString().trim();
        String weightText = binding.weightInput.getText() == null ? ""
                : binding.weightInput.getText().toString().trim();
        String setsText = binding.setsInput.getText() == null ? "" : binding.setsInput.getText().toString().trim();
        String repsText = binding.repsInput.getText() == null ? "" : binding.repsInput.getText().toString().trim();
        String notes = binding.notesInput.getText() == null ? "" : binding.notesInput.getText().toString().trim();

        if (category.isEmpty()) {
            Toast.makeText(this, "Please select a muscle category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (exercise.isEmpty()) {
            Toast.makeText(this, "Please select an exercise", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (weightText.isEmpty()) {
            Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show();
            return;
        }
        if (setsText.isEmpty()) {
            Toast.makeText(this, "Please enter sets", Toast.LENGTH_SHORT).show();
            return;
        }
        if (repsText.isEmpty()) {
            Toast.makeText(this, "Please enter reps", Toast.LENGTH_SHORT).show();
            return;
        }

        final Workout workout = new Workout();
        workout.setClientId(clientId);
        workout.setCategory(category);
        workout.setExerciseName(exercise);
        workout.setDate(date);
        workout.setWeightUsed(parseDouble(weightText));
        workout.setSets(parseInt(setsText));
        workout.setReps(parseInt(repsText));
        workout.setNotes(notes);

        updateSaveState(true);
        binding.getRoot()
                .post(() -> workoutRepository.getWorkoutsForClient(clientId, new DataCallback<List<Workout>>() {
                    @Override
                    public void onSuccess(List<Workout> data) {
                        double previousMax = 0d;
                        for (Workout item : data) {
                            if (workout.getExerciseName().equalsIgnoreCase(item.getExerciseName())
                                    && item.getWeightUsed() > previousMax) {
                                previousMax = item.getWeightUsed();
                            }
                        }
                        workout.setPersonalRecord(
                                PersonalRecordDetector.isNewRecord(workout.getWeightUsed(), previousMax));
                        workoutRepository.saveWorkout(workout, new ActionCallback() {
                            @Override
                            public void onSuccess() {
                                updateSaveState(false);
                                Snackbar.make(binding.getRoot(), "Workout saved successfully", Snackbar.LENGTH_SHORT)
                                        .show();
                                finish();
                            }

                            @Override
                            public void onError(String message) {
                                updateSaveState(false);
                                Toast.makeText(WorkoutEntryActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        updateSaveState(false);
                        Toast.makeText(WorkoutEntryActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void updateSaveState(boolean isSaving) {
        saving = isSaving;
        binding.saveWorkoutButton.setEnabled(!isSaving);
        binding.saveWorkoutButton.setText(isSaving ? "Saving..." : "Save Workout");
        binding.savingIndicator.setVisibility(isSaving ? View.VISIBLE : View.GONE);
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
