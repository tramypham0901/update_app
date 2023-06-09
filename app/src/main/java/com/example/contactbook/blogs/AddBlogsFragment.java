package com.example.contactbook.blogs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contactbook.PostActivity;
import com.example.contactbook.R;
import com.example.contactbook.model.Post;
import com.example.contactbook.model.User;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.PostService;
import com.example.contactbook.service.UserService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBlogsFragment extends Fragment {

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseAuth firebaseAuth;
    EditText title, des;
    ImageView image;
    String userCode, className, teacherCode, teacherName;

    String userFullname = "";
    Button upload;

    PostService postService;

    UserService userService;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button btnSelect, btnUpload;
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    public AddBlogsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_add_blogs, container, false);
        init();
        title = view.findViewById(R.id.ptitle);
        des = view.findViewById(R.id.pdes);
        image = view.findViewById(R.id.imagep);
        upload = view.findViewById(R.id.pupload);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        setCurrentUser();

        // After click on image we will be selecting an image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        // Now we will upload out blog
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titl = "" + title.getText().toString().trim();
                String description = "" + des.getText().toString().trim();

                // If empty set error
                if (TextUtils.isEmpty(titl)) {
                    title.setError("Title Cant be empty");
                    Toast.makeText(getContext(), "Title can't be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                // If empty set error
                if (TextUtils.isEmpty(description)) {
                    des.setError("Description Cant be empty");
                    Toast.makeText(getContext(), "Description can't be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                Post post = new Post();
                post.setPostName(titl);
                post.setDescription(description);
                post.setCreatedBy(userFullname);
                Call<Post> call = postService.addUser(post);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        Toast.makeText(getActivity(), "Tạo bài viết thành công!", Toast.LENGTH_SHORT).show();
                        moveToPostActivity();
                        uploadImage(response.body().getPostId(), response.body().getPostDate());
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {

                    }
                });

            }
        });
        return view;
    }

    private void init() {
        postService = APIUtils.getPostService();
        userService = APIUtils.getUserService();
        userCode = this.getArguments().getString("userCode");
        className = this.getArguments().getString("className");
        if (userCode.contains("HS")) {
            teacherCode = this.getArguments().getString("visit_user_id");
            teacherName = this.getArguments().getString("visit_user_name");
        }
    }

    private void setCurrentUser() {
        Call<User> call = userService.getUser(userCode);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userFullname = response.body().getFirstName() + " " + response.body().getLastName();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // UploadImage method
    private void uploadImage(long id, String postTime) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child("images/" + "img-"+id+"-"+postTime);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

//    // Here we are getting data from image
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == getActivity().RESULT_OK) {
//            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
//                imageuri = data.getData();
//                image.setImageURI(imageuri);
//            }
//            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
//                image.setImageURI(imageuri);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    // Override onActivityResult method
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == getActivity().RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContext().getContentResolver(),
                                filePath);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void moveToPostActivity() {
        Intent moveToSchedule = new Intent(getContext(), PostActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        moveToSchedule.putExtra("visit_user_id", teacherCode);
        moveToSchedule.putExtra("visit_user_name", teacherName);
        startActivity(moveToSchedule);
    }
}