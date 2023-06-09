package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactbook.markFragment.FinalFragment;
import com.example.contactbook.markFragment.Semester1Fragment;
import com.example.contactbook.markFragment.Semester2Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MarkActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    FrameLayout simpleFrameLayout;
    Fragment fragmentBottom = new FirstFragment();

    Fragment fragment = new Semester1Fragment();
    String userCode, className, teacherCode, teacherName;
    private BottomNavigationView navigationView;

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
        ft.replace(R.id.simpleFrameLayout, fragmentBottom);
        ft.remove(fragment);
        tabLayout.removeAllTabs();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void setFragment(int index) {
        switch (index) {
            case 0:
                fragment = new Semester1Fragment();
                break;
            case 1:
                fragment = new Semester2Fragment();
                break;
            case 2:
                fragment = new FinalFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userCode", userCode);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleMarkFrameLayout, fragment);
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
        firstTab.setText("Học kì I");
        tabLayout.addTab(firstTab);
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Học kì II");
        tabLayout.addTab(secondTab);
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Tổng kết");
        tabLayout.addTab(thirdTab);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        userCode = getIntent().getExtras().getString("userCode");
        className = getIntent().getExtras().getString("className");
        if (userCode.contains("HS")) {
            teacherCode = getIntent().getExtras().getString("visit_user_id");
            teacherName = getIntent().getExtras().getString("visit_user_name");
        }
        createTabs();
        navigationView.setOnNavigationItemSelectedListener(this);
        //navigationView.setSelectedItemId(R.id.home_navi);
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_navi) {
            moveToHomeActivity();
            return true;
        } else if (item.getItemId() == R.id.user_navi) {
            moveToProfileActivity();
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

    private void moveToHomeActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), HomeActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        startActivity(moveToSchedule);
    }

    private void moveToProfileActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), ProfileActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        startActivity(moveToSchedule);
    }

    private void moveToScheduleActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), TimetableActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        startActivity(moveToSchedule);
    }
}