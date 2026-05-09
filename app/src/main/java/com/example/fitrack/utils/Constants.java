package com.example.fitrack.utils;

import java.util.Arrays;
import java.util.List;

public final class Constants {
    public static final String COLLECTION_ADMINS = "admins";
    public static final String COLLECTION_CLIENTS = "clients";
    public static final String COLLECTION_WORKOUTS = "workouts";
    public static final String COLLECTION_ATTENDANCE = "attendance";
    public static final String COLLECTION_WEIGHT_TRACKING = "weight_tracking";
    public static final String COLLECTION_SCHEDULES = "schedules";
    public static final String COLLECTION_PROGRESS_PHOTOS = "progress_photos";
    public static final String COLLECTION_PERSONAL_RECORDS = "personal_records";

    public static final String EXTRA_CLIENT_ID = "extra_client_id";
    public static final String EXTRA_CLIENT_NAME = "extra_client_name";

    public static final String GOAL_WEIGHT_LOSS = "Weight Loss";
    public static final String GOAL_MUSCLE_GAIN = "Muscle Gain";
    public static final String GOAL_FAT_LOSS = "Fat Loss";
    public static final String GOAL_STRENGTH = "Strength Training";

    public static final List<String> CLIENT_GOALS = Arrays.asList(
            GOAL_WEIGHT_LOSS,
            GOAL_MUSCLE_GAIN,
            GOAL_FAT_LOSS,
            GOAL_STRENGTH);

    public static final List<String> WORKOUT_CATEGORIES = Arrays.asList(
            "Chest",
            "Back",
            "Biceps",
            "Triceps",
            "Shoulder",
            "Legs",
            "Abs");

    private Constants() {
    }
}

