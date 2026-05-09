package com.example.fitrack.repositories;

import com.example.fitrack.local.LocalStore;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Attendance;
import com.example.fitrack.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {
    public void markAttendance(Attendance attendance, final ActionCallback callback) {
        String attendanceId = attendance.getClientId() + "_" + attendance.getDate();
        attendance.setId(attendanceId);
        LocalStore.save(Constants.COLLECTION_ATTENDANCE, attendanceId, attendance);
        callback.onSuccess();
    }

    public void getAttendanceForClient(String clientId, final DataCallback<List<Attendance>> callback) {
        List<Attendance> attendanceList = LocalStore.whereEqualTo(Constants.COLLECTION_ATTENDANCE, "clientId",
                clientId);
        callback.onSuccess(attendanceList);
    }
}
