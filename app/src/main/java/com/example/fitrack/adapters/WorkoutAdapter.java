package com.example.fitrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrack.R;
import com.example.fitrack.models.WorkoutExercise;
import com.example.fitrack.models.WorkoutSession;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<WorkoutSession> sessions = new ArrayList<>();

    public void submitList(List<WorkoutSession> list) {
        this.sessions = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_timeline, parent, false);
        return new WorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutSession session = sessions.get(position);
        holder.focusText.setText(session.getFocus());
        holder.dateText.setText(session.getDate());

        StringBuilder summary = new StringBuilder();
        if (session.getExercises() != null) {
            for (int i = 0; i < session.getExercises().size(); i++) {
                WorkoutExercise ex = session.getExercises().get(i);
                summary.append(ex.getName())
                       .append(": ")
                       .append(ex.getSets())
                       .append("x")
                       .append(ex.getReps())
                       .append(" (")
                       .append(ex.getWeight())
                       .append("kg)");
                if (i < session.getExercises().size() - 1) {
                    summary.append("\n");
                }
            }
        }
        holder.summaryText.setText(summary.toString());
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView focusText;
        TextView dateText;
        TextView summaryText;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            focusText = itemView.findViewById(R.id.sessionFocusText);
            dateText = itemView.findViewById(R.id.sessionDateText);
            summaryText = itemView.findViewById(R.id.sessionExercisesSummaryText);
        }
    }
}
