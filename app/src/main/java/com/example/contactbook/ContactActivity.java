package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactbook.model.ClassModel;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.ClassService;
import com.example.contactbook.service.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    List<String> codes = new ArrayList<>();
    ListView listView;
    String userCode;
    private UserService userService;
    private ClassService classService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        userService = APIUtils.getUserService();
        classService = APIUtils.getClassService();
        userCode = getIntent().getExtras().get("userCode").toString();
        listView = (ListView) findViewById(R.id.contact_list);
        getClassByTeacher(userCode);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String val = (String) parent.getItemAtPosition(position);
                Intent moveToChat = new Intent(getApplicationContext(), ChatActivity.class);
                moveToChat.putExtra("userCode", userCode);
                moveToChat.putExtra("visit_user_id", val.replaceAll(" - .+", ""));
                moveToChat.putExtra("visit_user_name", val.replaceAll(".+ - ", ""));
                startActivity(moveToChat);
            }
        });
    }


    private void getClassByTeacher(String code) {
        Call<ClassModel> call = classService.getClassByTeacher(userCode);
        call.enqueue(new Callback<ClassModel>() {
            @Override
            public void onResponse(Call<ClassModel> call, Response<ClassModel> response) {
                for (int i = 0; i < response.body().getListStudentCode().size(); i++) {
                    String data = response.body().getListStudentCode().get(i) + " - " + response.body().getListStudentName().get(i);
                    codes.add(data);
                }
                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_contact_list, R.id.simpleContactItem, codes));
            }

            @Override
            public void onFailure(Call<ClassModel> call, Throwable t) {

            }
        });

    }
}