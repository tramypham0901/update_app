package com.example.contactbook.markFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.contactbook.R;
import com.example.contactbook.custom.FinalMarkAdapter;
import com.example.contactbook.model.Mark;
import com.example.contactbook.model.SortMark;
import com.example.contactbook.remote.APIUtils;
import com.example.contactbook.service.MarkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalFragment extends Fragment {
    List<Mark> marks = new ArrayList<>();
    List<SortMark> newMarks = new ArrayList<>();
    ListView listView;
    String userCode;
    private MarkService markService;

    public FinalFragment() {
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
        View contentView = inflater.inflate(R.layout.fragment_final, container, false);
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
                    marks = response.body();
                    List<SortMark> sortMarks = setSortMarks(response.body());
                    listView.setAdapter(new FinalMarkAdapter(getContext(), R.layout.custome_ds_final_mark, sortMarks));
                }
            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public List<SortMark> setSortMarks(List<Mark> list) {
        List<SortMark> sortMarks = new ArrayList<>();
        list.forEach(mark -> {
            SortMark filterMark = sortMarks.stream().filter(el -> el.getSubjectName().equals(mark.getSubjectName())).findFirst().orElse(null);
            if(filterMark != null) {
                if(mark.getSemester().contains("1")){
                    filterMark.setMark1S(""+mark.getSemesterMark());
                }
                else{
                    filterMark.setMark2S(""+mark.getSemesterMark());
                }
                if(filterMark.getMark1S().length() > 0 && filterMark.getMark2S().length() > 0) {
                    filterMark.setFinalMark(""+getFinalMark(list, filterMark.getSubjectName()));
                    filterMark.setFeedback(getFinalFeedback(Double.parseDouble(filterMark.getFinalMark())));
                }
            }
            else {
                SortMark sortMark = new SortMark();
                sortMark.setMark2S("");
                sortMark.setMark1S("");
                sortMark.setSubjectName(mark.getSubjectName());
                if(mark.getSemester().contains("1")){
                    sortMark.setMark1S(""+mark.getSemesterMark());
                }
                else{
                    sortMark.setMark2S(""+mark.getSemesterMark());
                }
                sortMark.setTeacherName(mark.getTeacherName());
                sortMark.setUserCode(mark.getStudentCode());
                sortMarks.add(sortMark);
            }
        });
        return sortMarks;
    }

    public String getFinalFeedback(double finalMark) {
        if(finalMark < 5) {
            return "Chưa đạt";
        }
        else if(finalMark >= 5 && finalMark < 7) {
            return "Hoàn thành";
        }
        else if(finalMark >= 7 && finalMark < 9) {
            return "Tốt";
        }
        else if(finalMark >= 9) {
            return "Xuất sắc";
        }
        else {
            return "";
        }
    }

    public double getFinalMark(List<Mark> list, String subjectName) {
        Map<String, List<Mark>> markGroup = list.stream().collect(Collectors.groupingBy(m -> m.getSubjectName()));
        List<Mark> keyList = markGroup.get(subjectName);
        double s1 = 0;
        double s2 = 0;
        for (Mark m : keyList) {
            if(m.getSemester().contains("1")){
                s1 = m.getSemesterMark();
            }
            if(m.getSemester().contains("2")){
                s2 = m.getSemesterMark();
            }
        }
        double finalMark = (s1 + (s2 * 2)) / 3;
        return ((double) Math.round(finalMark * 10) / 10);
    }

}