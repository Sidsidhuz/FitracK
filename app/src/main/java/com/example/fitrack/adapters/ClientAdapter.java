package com.example.fitrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrack.R;
import com.example.fitrack.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    public interface OnClientActionListener {
        void onClientClick(Client client);

        void onClientLongClick(Client client);
    }

    private final List<Client> clients = new ArrayList<>();
    private final OnClientActionListener listener;

    public ClientAdapter(OnClientActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Client> items) {
        clients.clear();
        if (items != null) {
            clients.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client_card, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.name.setText(client.getFullName());
        holder.goal.setText(client.getGoal());
        holder.weight.setText(String.format("Weight: %.1fkg", client.getWeight()));
        holder.attendance.setText(String.format("Attendance: %.0f%%", client.getAttendancePercentage()));
        holder.lastWorkout
                .setText("Last Workout: " + (client.getLastWorkout() == null ? "-" : client.getLastWorkout()));
        holder.streak.setText(String.format("Streak: %d days", client.getWorkoutStreak()));
        holder.card.setOnClickListener(v -> listener.onClientClick(client));
        holder.card.setOnLongClickListener(v -> {
            listener.onClientLongClick(client);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        final CardView card;
        final ImageView avatar;
        final TextView name;
        final TextView goal;
        final TextView weight;
        final TextView attendance;
        final TextView lastWorkout;
        final TextView streak;

        ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.clientCard);
            avatar = itemView.findViewById(R.id.clientAvatar);
            name = itemView.findViewById(R.id.clientName);
            goal = itemView.findViewById(R.id.clientGoal);
            weight = itemView.findViewById(R.id.clientWeight);
            attendance = itemView.findViewById(R.id.clientAttendance);
            lastWorkout = itemView.findViewById(R.id.clientLastWorkout);
            streak = itemView.findViewById(R.id.clientStreak);
        }
    }
}

