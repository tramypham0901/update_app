package com.example.contactbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactbook.custom.AttendAdapter;
import com.example.contactbook.model.Attendance;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.AttendanceService;
import com.example.contactbook.service.ClassService;
import com.example.contactbook.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FrameLayout simpleFrameLayout;
    Fragment fragmentBottom = new FirstFragment();
    String userCode, className, teacherCode, teacherName, fromDate, toDate;

    TextView txtScheduleDate, txtScheduleTo;

    Button btnFilter;

    int setFromTo = 1;
    private BottomNavigationView navigationView;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    LinearLayoutCompat layoutCompat;
    LinearLayout linearLayout;
    private DrawerLayout drawerLayout;

    private UserService userService;
    private ClassService classService;
    List<Attendance> attendanceList;
    private AttendanceService attendanceService;

    private ListView listView;

    private void setFragmentBottom(int index) {
        switch (index) {
            case 0:
                fragmentBottom = new FirstFragment();
                break;
            case 1:
                fragmentBottom = new SecondFragment();
                break;
            case 2:
                fragmentBottom = new ThirdFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userCode", userCode);
        bundle.putString("visit_user_id", teacherCode);
        bundle.putString("visit_user_name", teacherName);
        fragmentBottom.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        layoutCompat.setVisibility(View.GONE);
        linearLayout.setVisibility(View.INVISIBLE);
        ft.replace(R.id.simpleFrameLayout, fragmentBottom);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sổ liên lạc điện tử");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        txtScheduleDate = findViewById(R.id.txtScheduleDate);
        txtScheduleTo = findViewById(R.id.txtScheduleTo);
        btnFilter = findViewById(R.id.btnFilter);
        layoutCompat = findViewById(R.id.linear_schedule_date);
        layoutCompat.setVisibility(View.VISIBLE);
        linearLayout = findViewById(R.id.linear_attend_list);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        userCode = getIntent().getExtras().getString("userCode");
        className = getIntent().getExtras().getString("className");
        if (userCode.contains("HS")) {
            teacherCode = getIntent().getExtras().getString("visit_user_id");
            teacherName = getIntent().getExtras().getString("visit_user_name");
        }
        listView = (ListView) findViewById(R.id.attendList);
        init();
        attendanceService = APIUtils.getAttendanceService();
        navigationView.setOnNavigationItemSelectedListener(this);
        //navigationView.setSelectedItemId(R.id.home_navi);
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);

        txtScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromTo = 1;
                setDate(v);
            }
        });

        txtScheduleTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromTo = 2;
                setDate(v);
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDate = txtScheduleDate.getText().toString();
                toDate = txtScheduleTo.getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                if(LocalDate.parse(toDate, formatter).isBefore(LocalDate.parse(fromDate, formatter))){
                    Toast.makeText(AttendanceActivity.this, "Vui lòng chọn lại khoảng ngày", Toast.LENGTH_SHORT).show();
                }
                else {
                    getAttendanceList(userCode);
                }
            }
        });
    }

    private void getAttendanceList(String userCode) {
        Call<List<Attendance>> call = attendanceService.getByUser(userCode);
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                Map<String, List<Attendance>> attendGroup = response.body().stream().collect(Collectors.groupingBy(m -> m.getScheduleTime()));
                List<Attendance> newList = new ArrayList<>();
                attendGroup.forEach((s, attendances) -> {
                    newList.add(attendances.get(0));
                });
                attendanceList = newList.stream().filter(el -> {
                    LocalDate date = LocalDate.parse(el.getScheduleTime());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                    if(fromDate != null && toDate != null) {
                        if(date.isBefore(LocalDate.parse(fromDate, formatter)) || date.isAfter(LocalDate.parse(toDate, formatter))){
                            return false;
                        }
                        else {
                            return true;
                        }
                    }
                    else {
                        return date.equals(LocalDate.now());
                    }
                }).collect(Collectors.toList());
                listView.setAdapter(new AttendAdapter(AttendanceActivity.this, R.layout.custom_attend_list, attendanceList));
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_navi) {
            moveToHomeActivity();
            return true;
        } else if (item.getItemId() == R.id.user_navi) {
            setFragmentBottom(2);
            return true;
        } else if (item.getItemId() == R.id.logout_navi) {
            logout();
            return true;
        } else {
            return false;
        }
    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void moveToHomeActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), HomeActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        moveToSchedule.putExtra("visit_user_id", teacherCode);
        startActivity(moveToSchedule);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        if(setFromTo == 1) {
            txtScheduleDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
        else {
            txtScheduleTo.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
    }
}