package com.example.fitrack.repositories;

import com.example.fitrack.local.LocalStore;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Workout;
import com.example.fitrack.utils.Constants;
import com.example.fitrack.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorkoutRepository {
    public void saveWorkout(Workout workout, final ActionCallback callback) {
        String workoutId = LocalStore.generateId(Constants.COLLECTION_WORKOUTS);
        workout.setId(workoutId);
        LocalStore.save(Constants.COLLECTION_WORKOUTS, workoutId, workout);
        callback.onSuccess();
    }

    public void getWorkoutsForClient(String clientId, final DataCallback<List<Workout>> callback) {
        List<Workout> workouts = LocalStore.whereEqualTo(Constants.COLLECTION_WORKOUTS, "clientId", clientId);
        sortByDateDescending(workouts);
        callback.onSuccess(workouts);
    }

    public void searchWorkouts(String clientId, String query, final DataCallback<List<Workout>> callback) {
        getWorkoutsForClient(clientId, new DataCallback<List<Workout>>() {
            @Override
            public void onSuccess(List<Workout> workouts) {
                if (query == null || query.trim().isEmpty()) {
                    callback.onSuccess(workouts);
                    return;
                }
                String keyword = query.trim().toLowerCase();
                List<Workout> filtered = new ArrayList<>();
                for (Workout workout : workouts) {
                    String exercise = workout.getExerciseName() == null ? "" : workout.getExerciseName().toLowerCase();
                    String category = workout.getCategory() == null ? "" : workout.getCategory().toLowerCase();
                    if (exercise.contains(keyword) || category.contains(keyword)) {
                        filtered.add(workout);
                    }
                }
                callback.onSuccess(filtered);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void sortByDateDescending(List<Workout> workouts) {
        Collections.sort(workouts, new Comparator<Workout>() {
            @Override
            public int compare(Workout left, Workout right) {
                long leftTime = DateUtils.toTime(left.getDate());
                long rightTime = DateUtils.toTime(right.getDate());
                return Long.compare(rightTime, leftTime);
            }
        });
    }
}
