package com.example.fitrack.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class WorkoutCatalog {
    private static final Map<String, List<String>> EXERCISES_BY_CATEGORY = new LinkedHashMap<>();

    static {
        EXERCISES_BY_CATEGORY.put("Chest", Arrays.asList(
                "Bench Press",
                "Chest Fly",
                "Dumbbell Press",
                "Decline Chest Press",
                "Incline Chest Press",
                "Pushups"));
        EXERCISES_BY_CATEGORY.put("Back", Arrays.asList(
                "Lat Pulldown",
                "Back Pull",
                "Bent Over Row",
                "Deadlift",
                "Reverse Bent Over Row"));
        EXERCISES_BY_CATEGORY.put("Biceps", Arrays.asList(
                "Dumbbell Curl",
                "Hammer Curl",
                "Bar Curl",
                "Reverse Curl"));
        EXERCISES_BY_CATEGORY.put("Triceps", Arrays.asList(
                "Tricep Pulldown",
                "Tricep Kickback",
                "Overhead Extension",
                "Reverse Pulldown",
                "Dips",
                "Cross Over Tricep",
                "Dumbbell Back Push"));
        EXERCISES_BY_CATEGORY.put("Shoulder", Arrays.asList(
                "Dumbbell Press",
                "Lateral Raise",
                "Front Dumbbell Raise",
                "Barbell Back Press",
                "Upright Row",
                "Shrugs",
                "Face Pull"));
        EXERCISES_BY_CATEGORY.put("Legs", Arrays.asList(
                "Squats",
                "Sumo Squats",
                "Hack Squat",
                "Leg Extension",
                "Hamstring Curl",
                "Calf Raise"));
        EXERCISES_BY_CATEGORY.put("Abs", Arrays.asList(
                "Crunches",
                "Plank",
                "Leg Raise",
                "Russian Twist",
                "Mountain Climber"));
    }

    private WorkoutCatalog() {
    }

    public static List<String> getCategories() {
        return new ArrayList<>(EXERCISES_BY_CATEGORY.keySet());
    }

    public static List<String> getExercises(String category) {
        List<String> exercises = EXERCISES_BY_CATEGORY.get(category);
        return exercises == null ? new ArrayList<String>() : new ArrayList<>(exercises);
    }
}

