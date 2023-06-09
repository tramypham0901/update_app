package com.example.contactbook;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactbook.custom.PostAdapter;
import com.example.contactbook.model.Post;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.PostService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListFragment extends Fragment {

    RecyclerView recyclerView;
    List<Post> posts;
    PostAdapter postAdapter;
    PostService postService;

    String userCode, teacherCode, className, teacherName;

    List<String> imageList = new ArrayList<>();

    ProgressBar progressBar;

    public PostListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        userCode = this.getArguments().getString("userCode");
        className = this.getArguments().getString("className");
        if (userCode.contains("HS")) {
            teacherCode = this.getArguments().getString("visit_user_id");
            teacherName = this.getArguments().getString("visit_user_name");
        }
        postService = APIUtils.getPostService();

        recyclerView = view.findViewById(R.id.postrecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("images");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference file:listResult.getItems()){
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageList.add(uri.toString());
                            System.out.println(uri.toString());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            System.out.println(imageList.size());
                            getAllPost();
                        }
                    });
                }

                progressBar.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void getAllPost() {
        Call<List<Post>> call = postService.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                posts = response.body();
                postAdapter = new PostAdapter(getContext(), response.body(), imageList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}