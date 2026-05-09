package com.example.fitrack.repositories;

import com.example.fitrack.local.LocalStore;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.WeightProgress;
import com.example.fitrack.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class WeightRepository {
    public void saveWeight(WeightProgress progress, final ActionCallback callback) {
        String recordId = LocalStore.generateId(Constants.COLLECTION_WEIGHT_TRACKING);
        progress.setId(recordId);
        LocalStore.save(Constants.COLLECTION_WEIGHT_TRACKING, recordId, progress);
        callback.onSuccess();
    }

    public void getWeightHistory(String clientId, final DataCallback<List<WeightProgress>> callback) {
        List<WeightProgress> history = LocalStore.whereEqualTo(Constants.COLLECTION_WEIGHT_TRACKING, "clientId",
                clientId);
        callback.onSuccess(history);
    }
}
