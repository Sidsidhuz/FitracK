package com.example.fitrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrack.R;
import com.example.fitrack.models.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private final List<Schedule> schedules = new ArrayList<>();

    public void submitList(List<Schedule> items) {
        schedules.clear();
        if (items != null) {
            schedules.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_day, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.day.setText(schedule.getDay());
        holder.focus.setText(schedule.getFocus());
        holder.notes.setText(
                schedule.getNotes() == null || schedule.getNotes().isEmpty() ? "No notes" : schedule.getNotes());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        final TextView day;
        final TextView focus;
        final TextView notes;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.scheduleDay);
            focus = itemView.findViewById(R.id.scheduleFocus);
            notes = itemView.findViewById(R.id.scheduleNotes);
        }
    }
}

