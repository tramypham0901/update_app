package com.example.contactbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.contactbook.model.User;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecondFragment extends Fragment {
    String userCode, teacherCode, className, teacherName;
    ImageButton chatBtn, postImgBtn;
    UserService userService;
    FirebaseAuth mAuthListener;
    DatabaseReference rootRef;
    User userInfo;
    private FirebaseAuth mAuth;

    public SecondFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_second, container, false);
        userCode = this.getArguments().getString("userCode");
        className = this.getArguments().getString("className");
        if (userCode.contains("HS")) {
            teacherCode = this.getArguments().getString("visit_user_id");
            teacherName = this.getArguments().getString("visit_user_name");
        }
        userService = APIUtils.getUserService();
        chatBtn = (ImageButton) contextView.findViewById(R.id.chatImgBtn);
        postImgBtn = (ImageButton) contextView.findViewById(R.id.postImgBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userCode.contains("HS")) {
                    moveToChatActivity();
                } else {
                    moveToContactActivity();
                }
            }
        });

        postImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPostActivity();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance("https://contact-book-chat-57229-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");
        return contextView;
    }

    private void moveToChatActivity() {
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra("userCode", userCode);
        i.putExtra("visit_user_id", teacherCode);
        i.putExtra("visit_user_name", teacherName);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    private void moveToContactActivity() {
        Intent i = new Intent(getActivity(), ContactActivity.class);
        i.putExtra("userCode", userCode);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    private void moveToMarkActivity() {
        Intent i = new Intent(getActivity(), MarkActivity.class);
        i.putExtra("userCode", userCode);
        i.putExtra("className", className);
        i.putExtra("visit_user_id", teacherCode);
        i.putExtra("visit_user_name", teacherName);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    private void moveToScheduleActivity() {
        Intent moveToSchedule = new Intent(getActivity(), TimetableActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        moveToSchedule.putExtra("visit_user_id", teacherCode);
        moveToSchedule.putExtra("visit_user_name", teacherName);
        startActivity(moveToSchedule);
    }

    private void moveToPostActivity() {
        Intent moveToSchedule = new Intent(getActivity(), PostActivity.class);
        moveToSchedule.putExtra("userCode", userCode);
        moveToSchedule.putExtra("className", className);
        moveToSchedule.putExtra("visit_user_id", teacherCode);
        moveToSchedule.putExtra("visit_user_name", teacherName);
        startActivity(moveToSchedule);
    }
}