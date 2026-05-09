package com.example.fitrack.repositories;

import com.example.fitrack.local.LocalStore;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Schedule;
import com.example.fitrack.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRepository {
    public void saveSchedule(Schedule schedule, final ActionCallback callback) {
        String scheduleId = LocalStore.generateId(Constants.COLLECTION_SCHEDULES);
        schedule.setId(scheduleId);
        LocalStore.save(Constants.COLLECTION_SCHEDULES, scheduleId, schedule);
        callback.onSuccess();
    }

    public void getSchedules(String clientId, final DataCallback<List<Schedule>> callback) {
        List<Schedule> schedules = LocalStore.whereEqualTo(Constants.COLLECTION_SCHEDULES, "clientId", clientId);
        callback.onSuccess(schedules);
    }
}
