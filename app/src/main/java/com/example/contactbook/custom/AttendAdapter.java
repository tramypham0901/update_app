package com.example.contactbook.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.contactbook.R;
import com.example.contactbook.model.Attendance;

import java.util.List;

public class AttendAdapter extends BaseAdapter {

    List<Attendance> items;
    private Context context;
    private int layout;

    public AttendAdapter(Context context, int layout, List<Attendance> items) {
        super();
        this.context = context;
        this.layout = layout;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AttendAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new AttendAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_attend_list, null);
            convertView.setTag(holder);
        } else {
            holder = (AttendAdapter.ViewHolder) convertView.getTag();
        }
        holder.txtTime = (TextView) convertView.findViewById(R.id.text_attend_date);
        holder.txtAttend = (TextView) convertView.findViewById(R.id.text_attend_status);

        Attendance schedule = items.get(position);
        // set text
        holder.txtTime.setText(schedule.getScheduleTime());
        if(schedule.getIsAttended().equals("true")){
            holder.txtAttend.setText("Có mặt");
            holder.txtAttend.setTextColor(Color.GREEN);
        }
        else {
            holder.txtAttend.setText("Vắng mặt");
            holder.txtAttend.setTextColor(Color.RED);
        }
        return convertView;
    }

    class ViewHolder {
        TextView txtTime, txtAttend;
    }
}
