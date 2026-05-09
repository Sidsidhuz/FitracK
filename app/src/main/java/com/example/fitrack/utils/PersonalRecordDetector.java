package com.example.fitrack.utils;

public final class PersonalRecordDetector {
    private PersonalRecordDetector() {
    }

    public static boolean isNewRecord(double currentWeight, double previousMax) {
        return currentWeight > previousMax;
    }

    public static String buildRecordMessage(String exerciseName, double weightUsed) {
        return "New " + exerciseName + " Record Achieved! " + weightUsed + "kg";
    }
}

