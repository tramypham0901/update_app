package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactbook.blogs.AddBlogsFragment;
import com.example.contactbook.model.User;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.ClassService;
import com.example.contactbook.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TabLayout tabLayout;
    FrameLayout simpleFrameLayout;
    Fragment fragment = new FirstFragment();
    String userCode;
    User userInfo, teacher;
    private DrawerLayout drawerLayout;
    private BottomNavigationView navigationView;
    private UserService userService;
    private ClassService classService;

    private void setFragment(int index) {
        switch (index) {
            case 0:
                fragment = new FirstFragment();
                break;
            case 1:
                fragment = new AddBlogsFragment();
                break;
            case 2:
                fragment = new ThirdFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userCode", userCode);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        userCode = getIntent().getExtras().getString("userCode");
        userService = APIUtils.getUserService();
        classService = APIUtils.getClassService();
        getUserByCode(userCode);
        init();
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.home_navi);
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_navi) {
            setFragment(0);
            return true;
        } else if (item.getItemId() == R.id.logout_navi) {
            logout();
            return true;
        } else if (item.getItemId() == R.id.user_navi) {
            moveToProfileActivity();
            return true;
        } else {
            return false;
        }
    }

    private void moveToProfileActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), ProfileActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        startActivity(moveToSchedule);
    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void getUserByCode(String code) {
        Call<User> call = userService.getUser(code);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userInfo = response.body();
                } else {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không tìm thấy user " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}