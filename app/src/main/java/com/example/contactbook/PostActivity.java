package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactbook.blogs.AddBlogsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = new FirstFragment();

    String userCode, teacherCode, className, teacherName;

    private BottomNavigationView navigationView;

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sổ liên lạc điện tử");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        userCode = getIntent().getExtras().getString("userCode");
        className = getIntent().getExtras().getString("className");
        if (userCode.contains("HS")) {
            teacherCode = getIntent().getExtras().getString("visit_user_id");
            teacherName = getIntent().getExtras().getString("visit_user_name");
        }
        init();
        navigationView.setOnNavigationItemSelectedListener(this);
        setFragment(0);
    }

    private void setFragment(int index) {
        switch (index) {
            case 0:
                fragment = new PostListFragment();
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
        bundle.putString("className", className);
        bundle.putString("visit_user_id", teacherCode);
        bundle.putString("visit_user_name", teacherName);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
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
            moveToProfileActivity();
            return true;
        } else {
            return false;
        }
    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(userCode.contains("HS")){
            menu.getItem(0).setVisible(false);
        }

        // first parameter is the file for icon and second one is menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // We are using switch case because multiple icons can be kept
        if (item.getItemId() == R.id.shareButton) {
            setFragment(1);
        }
        return super.onOptionsItemSelected(item);
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
}