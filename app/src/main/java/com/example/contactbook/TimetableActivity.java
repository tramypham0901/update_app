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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactbook.scheduleFragment.WeekdayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class TimetableActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    FrameLayout simpleFrameLayout;
    Fragment fragmentBottom = new FirstFragment();

    Fragment fragment = new WeekdayFragment(WeekdayFragment.KEY_MONDAY_FRAGMENT);
    String userCode, className, teacherCode, teacherName, fromDate, toDate;

    TextView txtScheduleDate, txtScheduleTo;

    Button btnFilter;

    int setFromTo = 1;
    private BottomNavigationView navigationView;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    LinearLayoutCompat layoutCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        userCode = getIntent().getExtras().getString("userCode");
        className = getIntent().getExtras().getString("className");
        if (userCode.contains("HS")) {
            teacherCode = getIntent().getExtras().getString("visit_user_id");
            teacherName = getIntent().getExtras().getString("visit_user_name");
        }
        txtScheduleDate = findViewById(R.id.txtScheduleDate);
        txtScheduleTo = findViewById(R.id.txtScheduleTo);
        btnFilter = findViewById(R.id.btnFilter);
        layoutCompat = findViewById(R.id.linear_schedule_date);
        layoutCompat.setVisibility(View.VISIBLE);
        createTabs();
        navigationView.setOnNavigationItemSelectedListener(this);
        //navigationView.setSelectedItemId(R.id.home_navi);
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                setFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
            }
        });
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
                setFragment(0);
            }
        });
    }

    private void setFragmentBottom(int index) {
        switch (index) {
            case 0:
                fragmentBottom = new FirstFragment();
                break;
            case 1:

                break;
            case 2:
                fragmentBottom = new ThirdFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userCode", userCode);
        bundle.putString("className", className);
        bundle.putString("visit_user_id", teacherCode);
        bundle.putString("visit_user_name", teacherName);
        fragmentBottom.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragmentBottom);
        ft.remove(fragment);
        layoutCompat.setVisibility(View.GONE);
        tabLayout.removeAllTabs();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sổ liên lạc điện tử");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
    }

    private void createTabs() {
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Thứ 2");
        tabLayout.addTab(firstTab);
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Thứ 3");
        tabLayout.addTab(secondTab);
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Thứ 4");
        tabLayout.addTab(thirdTab);
        TabLayout.Tab fourthTab = tabLayout.newTab();
        fourthTab.setText("Thứ 5");
        tabLayout.addTab(fourthTab);
        TabLayout.Tab fifthTab = tabLayout.newTab();
        fifthTab.setText("Thứ 6");
        tabLayout.addTab(fifthTab);
        init();
    }

    private void setFragment(int index) {
        switch (index) {
            case 0:
                fragment = new WeekdayFragment(WeekdayFragment.KEY_MONDAY_FRAGMENT);
                break;
            case 1:
                fragment = new WeekdayFragment(WeekdayFragment.KEY_TUESDAY_FRAGMENT);
                break;
            case 2:
                fragment = new WeekdayFragment(WeekdayFragment.KEY_WEDNESDAY_FRAGMENT);
                break;
            case 3:
                fragment = new WeekdayFragment(WeekdayFragment.KEY_THURSDAY_FRAGMENT);
                break;
            case 4:
                fragment = new WeekdayFragment(WeekdayFragment.KEY_FRIDAY_FRAGMENT);
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userCode", userCode);
        bundle.putString("className", className);
        bundle.putString("teacherCode", teacherCode);
        if(fromDate != null && toDate != null) {
            bundle.putString("fromDate", fromDate);
            bundle.putString("toDate", toDate);
        }
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleScheduleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_navi) {
            moveToHomeActivity();
            return true;
        } else if (item.getItemId() == R.id.logout_navi) {
            logout();
            return true;
        } else if (item.getItemId() == R.id.user_navi) {
            setFragmentBottom(2);
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

    private void moveToHomeActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), HomeActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        startActivity(moveToSchedule);
    }

}