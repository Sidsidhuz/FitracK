package com.example.fitrack.utils;

import java.util.ArrayList;
import java.util.List;

public final class WorkoutCatalog {
        private WorkoutCatalog() {
        }

        public static List<String> getCategories() {
                return new ArrayList<>(ExerciseLibrary.categories());
        }

        public static List<String> getExercises(String category) {
                return new ArrayList<>(ExerciseLibrary.exercisesFor(category));
        }
}
