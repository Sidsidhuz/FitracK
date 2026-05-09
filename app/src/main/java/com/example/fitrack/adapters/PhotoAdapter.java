package com.example.fitrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrack.R;
import com.example.fitrack.models.ProgressPhoto;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private final List<ProgressPhoto> photos = new ArrayList<>();

    public void submitList(List<ProgressPhoto> items) {
        photos.clear();
        if (items != null) {
            photos.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        ProgressPhoto photo = photos.get(position);
        holder.title.setText(photo.getTitle());
        holder.type.setText(photo.getType());
        holder.date.setText(photo.getCapturedOn());
        holder.url.setText(photo.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView type;
        final TextView date;
        final TextView url;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.photoTitle);
            type = itemView.findViewById(R.id.photoType);
            date = itemView.findViewById(R.id.photoDate);
            url = itemView.findViewById(R.id.photoUrl);
        }
    }
}

