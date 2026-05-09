package com.example.fitrack.utils;

public final class FitnessCalculator {
    private FitnessCalculator() {
    }

    public static double bmi(double heightCm, double weightKg) {
        if (heightCm <= 0 || weightKg <= 0) {
            return 0d;
        }
        double heightMeters = heightCm / 100d;
        return weightKg / (heightMeters * heightMeters);
    }

    public static int attendancePercentage(int presentDays, int totalDays) {
        if (totalDays <= 0) {
            return 0;
        }
        return (int) Math.round((presentDays * 100d) / totalDays);
    }

    public static int completionPercentage(int completedWorkouts, int weeklyGoal) {
        if (weeklyGoal <= 0) {
            return 0;
        }
        int percentage = (int) Math.round((completedWorkouts * 100d) / weeklyGoal);
        return Math.min(100, Math.max(0, percentage));
    }
}

