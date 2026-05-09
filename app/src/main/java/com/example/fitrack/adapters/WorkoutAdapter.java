package com.example.fitrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrack.R;
import com.example.fitrack.models.Workout;
import com.example.fitrack.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private final List<Workout> workouts = new ArrayList<>();

    public void submitList(List<Workout> items) {
        workouts.clear();
        if (items != null) {
            workouts.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_timeline, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.date.setText(DateUtils.humanDate(workout.getDate()));
        holder.exercise.setText(workout.getExerciseName());
        holder.details.setText(String.format("%.1fkg | %d Sets x %d Reps", workout.getWeightUsed(), workout.getSets(),
                workout.getReps()));
        holder.notes
                .setText(workout.getNotes() == null || workout.getNotes().isEmpty() ? "No notes" : workout.getNotes());
        holder.category.setText(workout.getCategory());
        holder.record.setVisibility(workout.isPersonalRecord() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final TextView exercise;
        final TextView details;
        final TextView notes;
        final TextView category;
        final TextView record;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.workoutDate);
            exercise = itemView.findViewById(R.id.workoutExercise);
            details = itemView.findViewById(R.id.workoutDetails);
            notes = itemView.findViewById(R.id.workoutNotes);
            category = itemView.findViewById(R.id.workoutCategory);
            record = itemView.findViewById(R.id.workoutRecordBadge);
        }
    }
}

