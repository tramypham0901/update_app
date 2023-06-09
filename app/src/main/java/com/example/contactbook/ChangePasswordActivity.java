package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.contactbook.model.CheckPassword;
import com.example.contactbook.model.User;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FrameLayout simpleFrameLayout;
    String userCode;

    EditText txtPass, txtNewPass, txtConfirmPass;

    TextView txtChangePass;

    private UserService userService;

    User userInfo;

    private BottomNavigationView navigationView;

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sổ liên lạc điện tử");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        txtPass = findViewById(R.id.old_pass);
        txtNewPass = findViewById(R.id.new_pass);
        txtConfirmPass = findViewById(R.id.verify_pass);
        txtChangePass = findViewById(R.id.change_pass);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        userCode = getIntent().getExtras().getString("userCode");
        init();
        navigationView.setOnNavigationItemSelectedListener(this);
        userService = APIUtils.getUserService();
        getUserByCode(userCode);
        txtChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPass.getText() == null || txtNewPass.getText() == null || txtConfirmPass.getText() == null) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng điền đầy đủ các trường trên!", Toast.LENGTH_SHORT).show();
                }
                else if(txtPass.getText().length() < 1 || txtNewPass.length() < 1 || txtConfirmPass.length() < 1) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng điền đầy đủ các trường trên!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!txtNewPass.getText().toString().equals(txtConfirmPass.getText().toString())) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                    }
                    else if(txtNewPass.getText().toString().length() < 6) {
                        Toast.makeText(ChangePasswordActivity.this, "Độ dài mật khẩu tối thiểu là 6", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Call<CheckPassword> call = userService.checkPassword(new CheckPassword(userInfo.getUsername(), txtPass.getText().toString()));
                        call.enqueue(new Callback<CheckPassword>() {
                            @Override
                            public void onResponse(Call<CheckPassword> CheckPassword, Response<CheckPassword> response) {
                                if(response.body().getOldPassword() != null && response.body().getOldPassword().equals(txtPass.getText().toString())) {
                                    String newPass = txtNewPass.getText().toString();
                                    CheckPassword checkPassword = new CheckPassword(userInfo.getUsername(), txtPass.getText().toString(), newPass);
                                    changePassFirebase(checkPassword);
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckPassword> call, Throwable t) {
                                Toast.makeText(ChangePasswordActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    public void changePassFirebase (CheckPassword checkPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(checkPassword.getUsername()+"@gmail.com", checkPassword.getOldPassword());

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(checkPassword.getNewPassword()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        updatePass(checkPassword);
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updatePass(CheckPassword checkPassword){
        Call<Boolean> call = userService.changePassword(checkPassword);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                moveToProfileActivity();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
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

    private void moveToHomeActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), HomeActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        startActivity(moveToSchedule);
    }

    private void moveToProfileActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), ProfileActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        startActivity(moveToSchedule);
    }

}