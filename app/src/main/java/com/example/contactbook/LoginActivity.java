package com.example.contactbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactbook.model.JwtRequest;
import com.example.contactbook.model.JwtResponse;
import com.example.contactbook.model.User;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.AuthenticateService;
import com.example.contactbook.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Base64.Decoder decoder = Base64.getUrlDecoder();
    Button loginBtn;
    JSONObject json;
    EditText username, pass;
    User userInfo;
    String userCode;
    private UserService userService;
    private AuthenticateService authenticateService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        init();
        userService = APIUtils.getUserService();
        authenticateService = APIUtils.getAuthenticateService();
        loginBtn = (Button) findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void init() {
        username = (EditText) findViewById(R.id.editTextUsername);
        pass = (EditText) findViewById(R.id.editTextPassword);
    }

    public boolean validateRequest(String username, String password) {
        if (username.length() < 1 || password.length() < 1) {
            return false;
        }
        return true;
    }

    public void signInFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUserByCode(userCode);
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createFirebaseAcc() {
        String email = username.getText().toString() + "@gmail.com";
        String password = pass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signInFirebase(email, password);
                            Toast.makeText(LoginActivity.this, "Tạo tk chat thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                signInFirebase(email, password);
                            } catch (Exception e) {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void getUserByCode(String code) {
        Call<User> call = userService.getUser(code);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userInfo = response.body();
                    updateFirebaseUser();
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

    private void updateFirebaseUser() {
        String fullName = userInfo.getFirstName() + " " + userInfo.getLastName();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .setPhotoUri(Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/User-avatar.svg/2048px-User-avatar.svg.png"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
    }

    public void login() {
        if (validateRequest(username.getText().toString(), pass.getText().toString())) {
            JwtRequest request = new JwtRequest(username.getText().toString(), pass.getText().toString());
            Call<JwtResponse> call = authenticateService.login(request);
            call.enqueue(new Callback<JwtResponse>() {
                @Override
                public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                    if (response.isSuccessful()) {
                        String token = response.body().getJwtToken();
                        String[] splits = token.split("\\.");
                        String header = new String(decoder.decode(splits[0]));
                        String payload = new String(decoder.decode(splits[1]));
                        try {
                            json = new JSONObject(payload);
                            moveToHomepage();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Đăng nhập thất bại! Sai tên đăng nhập hoặc mk", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JwtResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ERROR: ", t.getMessage());
                }
            });
        }
    }

    private void moveToHomepage() {
        Intent moveToHomeIntent = new Intent(this, HomeActivity.class);
        Intent moveToTeacherIntent = new Intent(this, TeacherHomeActivity.class);
        moveToHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        moveToTeacherIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            userCode = json.get("userCode").toString();
            if (userCode.contains("AD") || userCode.contains("QL")) {
                Toast.makeText(this, "Vui lòng đăng nhập với account học sinh hoặc giáo viên", Toast.LENGTH_LONG).show();
            } else {
                createFirebaseAcc();
                moveToHomeIntent.putExtra("userCode", userCode);
                moveToTeacherIntent.putExtra("userCode", userCode);
                if (userCode.contains("HS")) {
                    startActivity(moveToHomeIntent);
                } else {
                    startActivity(moveToTeacherIntent);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}