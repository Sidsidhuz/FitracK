package com.example.fitrack.repositories;

import android.content.Context;
import android.net.Uri;

import com.example.fitrack.firebase.FirebaseManager;
import com.example.fitrack.local.LocalStore;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.ProgressPhoto;
import com.example.fitrack.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhotoRepository {
    public void uploadPhoto(Context context, final ProgressPhoto photo, Uri imageUri, final ActionCallback callback) {
        if (imageUri == null) {
            callback.onError("Image is required");
            return;
        }
        String imageName = UUID.randomUUID().toString() + ".jpg";
        // In demo mode we don't actually upload; just set a placeholder URL and save to LocalStore
        photo.setImageUrl("file://local/" + imageName);
        String photoId = LocalStore.generateId(Constants.COLLECTION_PROGRESS_PHOTOS);
        photo.setId(photoId);
        LocalStore.save(Constants.COLLECTION_PROGRESS_PHOTOS, photoId, photo);
        callback.onSuccess();
    }

    public void getPhotos(String clientId, final DataCallback<List<ProgressPhoto>> callback) {
        List<ProgressPhoto> photos = LocalStore.whereEqualTo(Constants.COLLECTION_PROGRESS_PHOTOS, "clientId",
                clientId);
        callback.onSuccess(photos);
    }
}
