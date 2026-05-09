package com.example.fitrack.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Attendance;
import com.example.fitrack.models.Client;
import com.example.fitrack.models.PersonalRecord;
import com.example.fitrack.models.ProgressPhoto;
import com.example.fitrack.models.Schedule;
import com.example.fitrack.models.WeightProgress;
import com.example.fitrack.models.Workout;
import com.example.fitrack.repositories.AttendanceRepository;
import com.example.fitrack.repositories.ClientRepository;
import com.example.fitrack.repositories.PhotoRepository;
import com.example.fitrack.repositories.ScheduleRepository;
import com.example.fitrack.repositories.WeightRepository;
import com.example.fitrack.repositories.WorkoutRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientProfileViewModel extends ViewModel {
    private final ClientRepository clientRepository = new ClientRepository();
    private final WorkoutRepository workoutRepository = new WorkoutRepository();
    private final AttendanceRepository attendanceRepository = new AttendanceRepository();
    private final WeightRepository weightRepository = new WeightRepository();
    private final ScheduleRepository scheduleRepository = new ScheduleRepository();
    private final PhotoRepository photoRepository = new PhotoRepository();

    private final MutableLiveData<Client> client = new MutableLiveData<>();
    private final MutableLiveData<List<Workout>> workouts = new MutableLiveData<>(new ArrayList<Workout>());
    private final MutableLiveData<List<Attendance>> attendance = new MutableLiveData<>(new ArrayList<Attendance>());
    private final MutableLiveData<List<WeightProgress>> weightHistory = new MutableLiveData<>(
            new ArrayList<WeightProgress>());
    private final MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>(new ArrayList<Schedule>());
    private final MutableLiveData<List<ProgressPhoto>> photos = new MutableLiveData<>(new ArrayList<ProgressPhoto>());

    public LiveData<Client> getClient() {
        return client;
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workouts;
    }

    public LiveData<List<Attendance>> getAttendance() {
        return attendance;
    }

    public LiveData<List<WeightProgress>> getWeightHistory() {
        return weightHistory;
    }

    public LiveData<List<Schedule>> getSchedules() {
        return schedules;
    }

    public LiveData<List<ProgressPhoto>> getPhotos() {
        return photos;
    }

    public void loadClient(final String clientId) {
        clientRepository.getClients(new DataCallback<List<Client>>() {
            @Override
            public void onSuccess(List<Client> data) {
                for (Client item : data) {
                    if (clientId.equals(item.getId())) {
                        client.setValue(item);
                        break;
                    }
                }
            }

            @Override
            public void onError(String message) {
                client.setValue(null);
            }
        });
    }

    public void loadWorkouts(String clientId) {
        workoutRepository.getWorkoutsForClient(clientId, new DataCallback<List<Workout>>() {
            @Override
            public void onSuccess(List<Workout> data) {
                workouts.setValue(data);
            }

            @Override
            public void onError(String message) {
                workouts.setValue(new ArrayList<Workout>());
            }
        });
    }

    public void loadAttendance(String clientId) {
        attendanceRepository.getAttendanceForClient(clientId, new DataCallback<List<Attendance>>() {
            @Override
            public void onSuccess(List<Attendance> data) {
                attendance.setValue(data);
            }

            @Override
            public void onError(String message) {
                attendance.setValue(new ArrayList<Attendance>());
            }
        });
    }

    public void loadWeightHistory(String clientId) {
        weightRepository.getWeightHistory(clientId, new DataCallback<List<WeightProgress>>() {
            @Override
            public void onSuccess(List<WeightProgress> data) {
                weightHistory.setValue(data);
            }

            @Override
            public void onError(String message) {
                weightHistory.setValue(new ArrayList<WeightProgress>());
            }
        });
    }

    public void loadSchedules(String clientId) {
        scheduleRepository.getSchedules(clientId, new DataCallback<List<Schedule>>() {
            @Override
            public void onSuccess(List<Schedule> data) {
                schedules.setValue(data);
            }

            @Override
            public void onError(String message) {
                schedules.setValue(new ArrayList<Schedule>());
            }
        });
    }

    public void loadPhotos(String clientId) {
        photoRepository.getPhotos(clientId, new DataCallback<List<ProgressPhoto>>() {
            @Override
            public void onSuccess(List<ProgressPhoto> data) {
                photos.setValue(data);
            }

            @Override
            public void onError(String message) {
                photos.setValue(new ArrayList<ProgressPhoto>());
            }
        });
    }
}

