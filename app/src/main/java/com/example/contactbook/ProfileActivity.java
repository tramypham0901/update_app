package com.example.contactbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.contactbook.model.User;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FrameLayout simpleFrameLayout;
    String userCode;

    TextView txtName, txtUsername, txtEmail;

    private ImageView close;
    private CircleImageView imageProfile;
    private TextView changePhoto, changePass;

    private UserService userService;

    User userInfo;

    private Uri mImageUri;

    private BottomNavigationView navigationView;

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sổ liên lạc điện tử");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        txtName = findViewById(R.id.fullname);
        txtUsername = findViewById(R.id.username);
        txtEmail = findViewById(R.id.email);
        changePhoto = findViewById(R.id.change_photo);
        changePass = findViewById(R.id.change_pass);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userCode = getIntent().getExtras().getString("userCode");
        init();
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.user_navi);
        userService = APIUtils.getUserService();
        getUserByCode(userCode);
        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(ProfileActivity.this);
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToChangePassActivity();
            }
        });
        //navigationView.setSelectedItemId(R.id.home_navi);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_navi) {
            moveToHomeActivity();
            return true;
        } else if (item.getItemId() == R.id.user_navi) {
            return true;
        } else if (item.getItemId() == R.id.logout_navi) {
            logout();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            //uploadImage();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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
                    txtName.setText(response.body().getFirstName()+ " " +response.body().getLastName());
                    txtUsername.setText(response.body().getUsername());
                    txtEmail.setText(response.body().getUsername()+"@gmail.com");
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
    private void moveToChangePassActivity() {
        Intent moveToSchedule = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        startActivity(moveToSchedule);
    }
}