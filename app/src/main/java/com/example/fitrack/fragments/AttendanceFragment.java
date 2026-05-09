package com.example.fitrack.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrack.adapters.CalendarAdapter;
import com.example.fitrack.databinding.FragmentAttendanceBinding;
import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.interfaces.DataCallback;
import com.example.fitrack.models.Attendance;
import com.example.fitrack.repositories.AttendanceRepository;
import com.example.fitrack.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AttendanceFragment extends BaseClientFragment implements CalendarAdapter.OnItemListener {
    private FragmentAttendanceBinding binding;
    private final AttendanceRepository attendanceRepository = new AttendanceRepository();
    private List<Attendance> allAttendance;

    private Calendar currentMonthCalendar;
    private Calendar selectedDateCalendar;
    private CalendarAdapter calendarAdapter;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.US);
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

    public static AttendanceFragment newInstance(String clientId) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentMonthCalendar = Calendar.getInstance();
        selectedDateCalendar = Calendar.getInstance();

        setMonthView();

        binding.prevMonthButton.setOnClickListener(v -> {
            currentMonthCalendar.add(Calendar.MONTH, -1);
            setMonthView();
        });

        binding.nextMonthButton.setOnClickListener(v -> {
            currentMonthCalendar.add(Calendar.MONTH, 1);
            setMonthView();
        });

        binding.presentButton.setOnClickListener(v -> markAttendance(true));
        binding.absentButton.setOnClickListener(v -> markAttendance(false));

        updateSelectedDateUI();
        loadAttendance();
    }

    private void setMonthView() {
        binding.monthYearText.setText(monthYearFormat.format(currentMonthCalendar.getTime()));
        ArrayList<String> daysInMonth = daysInMonthArray(currentMonthCalendar);

        calendarAdapter = new CalendarAdapter(daysInMonth, currentMonthCalendar, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);
        
        if (allAttendance != null) {
            calendarAdapter.setAttendanceList(allAttendance);
        }
        calendarAdapter.setSelectedDate(dateFormat.format(selectedDateCalendar.getTime()));
    }

    private ArrayList<String> daysInMonthArray(Calendar date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        Calendar monthStart = (Calendar) date.clone();
        monthStart.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = monthStart.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (dayText.isEmpty()) return;

        Calendar clickedDate = (Calendar) currentMonthCalendar.clone();
        clickedDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayText));

        Calendar today = Calendar.getInstance();
        if (clickedDate.after(today) && !isSameDay(clickedDate, today)) {
            Toast.makeText(requireContext(), "Cannot select future dates", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedDateCalendar = clickedDate;
        calendarAdapter.setSelectedDate(dateFormat.format(selectedDateCalendar.getTime()));
        updateSelectedDateUI();
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private void updateSelectedDateUI() {
        binding.selectedDateText.setText(displayFormat.format(selectedDateCalendar.getTime()));
        refreshStatus();
    }

    private void refreshStatus() {
        if (allAttendance == null) {
            binding.attendanceStatusText.setText("Status: Unmarked");
            binding.attendanceStatusText.setTextColor(0xFFA0AAB2); // Gray
            return;
        }

        String selStr = dateFormat.format(selectedDateCalendar.getTime());
        boolean found = false;
        for (Attendance att : allAttendance) {
            if (selStr.equals(att.getDate())) {
                found = true;
                if (att.isPresent()) {
                    binding.attendanceStatusText.setText("Status: PRESENT");
                    binding.attendanceStatusText.setTextColor(0xFF00E676); // fitness_primary
                } else {
                    binding.attendanceStatusText.setText("Status: ABSENT");
                    binding.attendanceStatusText.setTextColor(0xFFFF5252); // fitness_error
                }
                break;
            }
        }

        if (!found) {
            binding.attendanceStatusText.setText("Status: Unmarked");
            binding.attendanceStatusText.setTextColor(0xFFA0AAB2); // Gray
        }
    }

    private void loadAttendance() {
        if (clientId != null) {
            attendanceRepository.getAttendanceForClient(clientId, new DataCallback<List<Attendance>>() {
                @Override
                public void onSuccess(List<Attendance> data) {
                    allAttendance = data;
                    if (calendarAdapter != null) {
                        calendarAdapter.setAttendanceList(data);
                    }
                    refreshStatus();
                }

                @Override
                public void onError(String message) {
                    // Ignore
                }
            });
        }
    }

    private void markAttendance(boolean present) {
        if (clientId == null) {
            Toast.makeText(requireContext(), "Select a client first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!present) {
            checkForScheduleShift();
        } else {
            saveAttendance(true);
        }
    }

    private void checkForScheduleShift() {
        com.example.fitrack.repositories.WeeklyScheduleRepository scheduleRepo = new com.example.fitrack.repositories.WeeklyScheduleRepository();
        scheduleRepo.getWeeklySchedule(clientId, new DataCallback<com.example.fitrack.models.WeeklySchedule>() {
            @Override
            public void onSuccess(com.example.fitrack.models.WeeklySchedule schedule) {
                int dayOfWeek = selectedDateCalendar.get(Calendar.DAY_OF_WEEK);
                String focus = getFocusForDay(schedule, dayOfWeek);

                if (focus != null && !focus.equalsIgnoreCase("Rest Day") && dayOfWeek != Calendar.SUNDAY) {
                    new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Missed " + focus)
                        .setMessage("You missed a scheduled workout. Should we shift the entire weekly schedule forward by one day? (Sunday will remain a Rest Day)")
                        .setPositiveButton("Shift Schedule", (dialog, which) -> {
                            schedule.shiftSchedule(dayOfWeek);
                            scheduleRepo.saveWeeklySchedule(schedule, new ActionCallback() {
                                @Override
                                public void onSuccess() {
                                    saveAttendance(false);
                                    Toast.makeText(requireContext(), "Schedule shifted forward!", Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onError(String message) {
                                    saveAttendance(false);
                                }
                            });
                        })
                        .setNegativeButton("Just Mark Absent", (dialog, which) -> saveAttendance(false))
                        .show();
                } else {
                    saveAttendance(false);
                }
            }

            @Override
            public void onError(String message) {
                saveAttendance(false);
            }
        });
    }

    private String getFocusForDay(com.example.fitrack.models.WeeklySchedule s, int day) {
        switch (day) {
            case Calendar.MONDAY: return s.getMondayFocus();
            case Calendar.TUESDAY: return s.getTuesdayFocus();
            case Calendar.WEDNESDAY: return s.getWednesdayFocus();
            case Calendar.THURSDAY: return s.getThursdayFocus();
            case Calendar.FRIDAY: return s.getFridayFocus();
            case Calendar.SATURDAY: return s.getSaturdayFocus();
            case Calendar.SUNDAY: return s.getSundayFocus();
            default: return null;
        }
    }

    private void saveAttendance(boolean present) {
        Attendance attendance = new Attendance();
        attendance.setClientId(clientId);
        attendance.setDate(dateFormat.format(selectedDateCalendar.getTime()));
        attendance.setPresent(present);
        attendance.setNotes(present ? "Workout completed" : "Missed session");

        attendanceRepository.markAttendance(attendance, new ActionCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(requireContext(), "Attendance saved", Toast.LENGTH_SHORT).show();
                loadAttendance(); 
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
