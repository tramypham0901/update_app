package com.example.contactbook.scheduleFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.contactbook.R;
import com.example.contactbook.custom.ScheduleAdapter;
import com.example.contactbook.model.Attendance;
import com.example.contactbook.model.Schedule;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.AttendanceService;
import com.example.contactbook.service.ScheduleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekdayFragment extends Fragment {

    public static final int KEY_MONDAY_FRAGMENT = 1;
    public static final int KEY_TUESDAY_FRAGMENT = 2;
    public static final int KEY_WEDNESDAY_FRAGMENT = 3;
    public static final int KEY_THURSDAY_FRAGMENT = 4;
    public static final int KEY_FRIDAY_FRAGMENT = 5;
    private final int key;
    List<Schedule> scheduleList;
    List<Attendance> attendanceList;
    private String userCode, className, fromDate, toDate;
    private ListView listView;
    private ScheduleService scheduleService;
    private AttendanceService attendanceService;

    public WeekdayFragment() {
        super();
        this.key = KEY_MONDAY_FRAGMENT;
    }

    public WeekdayFragment(int key) {
        super();
        this.key = key;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekday, container, false);
        scheduleService = APIUtils.getScheduleService();
        attendanceService = APIUtils.getAttendanceService();
        listView = (ListView) view.findViewById(R.id.scheduleList);
        userCode = this.getArguments().getString("userCode");
        className = this.getArguments().getString("className");
        fromDate = this.getArguments().getString("fromDate");
        toDate = this.getArguments().getString("toDate");
        getSchedulesByClass(className);
        return view;
    }

    private void setAttendanceForSchedule(List<Attendance> list) {
        for (Schedule s : scheduleList) {
            for (Attendance a : list) {
                if (a.getScheduleId() == s.getScheduleId()) {
                    s.setAttendance(a.getIsAttended());
                }
            }
        }
        listView.setAdapter(new ScheduleAdapter(getContext(), R.layout.simple_schedule_item, scheduleList));
    }

    private void getAttendanceList(String userCode) {
        Call<List<Attendance>> call = attendanceService.getByUser(userCode);
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                attendanceList = response.body();
                setAttendanceForSchedule(response.body());
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {

            }
        });
    }

    private void checkUserAttend(String userCode, long scheduleId) {
        Call<Boolean> call = attendanceService.checkAttend(userCode, scheduleId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void getSchedulesByClass(String className) {
        Call<List<Schedule>> call = scheduleService.getScheduleByClass(className);
        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                scheduleList = response.body().stream().filter(s -> {
                    LocalDate date = LocalDate.parse(s.getScheduleTime());
                    int dayWeek = date.getDayOfWeek().getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                    if(fromDate != null && toDate != null) {
                        if(date.isBefore(LocalDate.parse(fromDate, formatter)) || date.isAfter(LocalDate.parse(toDate, formatter))){
                            return false;
                        }
                    }
                    return dayWeek == key;
                }).collect(Collectors.toList());
                getAttendanceList(userCode);
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {

            }
        });
    }
}