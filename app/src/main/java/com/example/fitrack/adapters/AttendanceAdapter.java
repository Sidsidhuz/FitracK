package com.example.fitrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrack.R;
import com.example.fitrack.models.Attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    private final List<Attendance> attendanceList = new ArrayList<>();

    public void submitList(List<Attendance> items) {
        attendanceList.clear();
        if (items != null) {
            attendanceList.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_day, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.date.setText(attendance.getDate());
        holder.status.setText(attendance.isPresent() ? "Present" : "Absent");
        holder.status.setTextColor(holder.itemView.getContext()
                .getColor(attendance.isPresent() ? android.R.color.holo_green_light : android.R.color.holo_red_light));
        holder.notes.setText(attendance.getNotes() == null ? "" : attendance.getNotes());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final TextView status;
        final TextView notes;

        AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.attendanceDate);
            status = itemView.findViewById(R.id.attendanceStatus);
            notes = itemView.findViewById(R.id.attendanceNotes);
        }
    }
}

