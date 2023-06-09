package com.example.contactbook.markFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.contactbook.R;
import com.example.contactbook.custom.CustomAdapter;
import com.example.contactbook.model.Mark;
import com.example.contactbook.model.SortMark;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.MarkService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Semester1Fragment extends Fragment {

    List<Mark> marks = new ArrayList<>();
    List<SortMark> newMarks = new ArrayList<>();
    ListView listView;
    String userCode;
    private MarkService markService;

    public Semester1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_semester1, container, false);
        userCode = this.getArguments().getString("userCode");
        listView = contentView.findViewById(R.id.mark_list);
        markService = APIUtils.getMarkService();
        getMarkList();
        return contentView;
    }

    public void getMarkList() {
        Call<List<Mark>> call = markService.getMarksByStudent(userCode);
        call.enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {
                if (response.isSuccessful()) {
                    marks = response.body().stream().filter(el -> el.getSemester().contains("1")).collect(Collectors.toList());
                    listView.setAdapter(new CustomAdapter(getContext(), R.layout.custom_ds_adapter_diemso, marks));
                }
            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }
}