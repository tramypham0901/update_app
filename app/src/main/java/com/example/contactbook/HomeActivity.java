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

import com.example.contactbook.model.ClassModel;
import com.example.contactbook.model.User;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.ClassService;
import com.example.contactbook.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TabLayout tabLayout;
    FrameLayout simpleFrameLayout;
    Fragment fragment = new FirstFragment();
    String userCode, teacherCode, className;
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
                fragment = new SecondFragment();
                break;
            case 2:
                fragment = new ThirdFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userCode", userCode);
        bundle.putString("className", className);
        bundle.putString("visit_user_id", teacherCode);
        if(teacher != null) {
            bundle.putString("visit_user_name", teacher.getFirstName() + " " + teacher.getLastName());
        }
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
    protected void onStart() {
        super.onStart();
        navigationView.setSelectedItemId(R.id.home_navi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userCode = getIntent().getExtras().getString("userCode");
        userService = APIUtils.getUserService();
        classService = APIUtils.getClassService();
        getUserByCode(userCode);
        init();
        navigationView.setOnNavigationItemSelectedListener(this);
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_navi) {
            setFragment(0);
            return true;
        } else if (item.getItemId() == R.id.user_navi) {
            //setFragment(1);
            moveToProfileActivity();
            return true;
        } else if (item.getItemId() == R.id.logout_navi) {
            logout();
            return true;
        } else {
            return false;
        }
    }

    public void getTeacherOfUser(String className) {
        if (className.length() > 0) {
            Call<ClassModel> call = classService.getClassByName(className);
            call.enqueue(new Callback<ClassModel>() {
                @Override
                public void onResponse(Call<ClassModel> call, Response<ClassModel> response) {
                    getTeacherByCode(response.body().getFormTeacherCode());
                    teacherCode = response.body().getFormTeacherCode();

                }

                @Override
                public void onFailure(Call<ClassModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy user " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getTeacherByCode(String code) {
        if (code != null) {
            Call<User> call = userService.getUser(code);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        teacher = response.body();
                        navigationView.setSelectedItemId(R.id.home_navi);
                    } else {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy user " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(getApplicationContext(), "User code null ", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserByCode(String code) {
        Call<User> call = userService.getUser(code);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userInfo = response.body();
                    if (response.body().getUserCode().contains("HS")) {
                        className = response.body().getStudentClass();
                        getTeacherOfUser(response.body().getStudentClass());
                    }
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

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void moveToProfileActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), ProfileActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        startActivity(moveToSchedule);
    }

    private void moveToPostActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), PostActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        startActivity(moveToSchedule);
    }
}
