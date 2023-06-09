package com.example.contactbook.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.contactbook.R;
import com.example.contactbook.model.Schedule;

import java.util.List;

public class ScheduleAdapter extends BaseAdapter {

    List<Schedule> items;
    private Context context;
    private int layout;

    public ScheduleAdapter(Context context, int layout, List<Schedule> items) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ScheduleAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_schedule_item, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtSubject = (TextView) convertView.findViewById(R.id.subject);
        holder.txtTeacher = (TextView) convertView.findViewById(R.id.teacher);
        holder.txtTime = (TextView) convertView.findViewById(R.id.time);
        holder.txtClass = (TextView) convertView.findViewById(R.id.room);

        Schedule schedule = items.get(position);
        // set text
        holder.txtSubject.setText(schedule.getSubjectName());
        holder.txtTeacher.setText("Giáo viên: " + schedule.getTeacherName());
        holder.txtTime.setText(schedule.getScheduleTime()+" "+schedule.getScheduleFrom() + " -> " + schedule.getScheduleTo());
        holder.txtClass.setText("Lớp " + schedule.getClassName());
//        if (position != 0) {
//            Schedule first = items.get(0);
//            if (first.getAttendance() == null) {
//                holder.txtAttend.setText("N/A");
//            } else if (first.getAttendance().equals("true")) {
//                holder.txtAttend.setText("Có mặt");
//                holder.txtAttend.setTextColor(Color.GREEN);
//            } else {
//                holder.txtAttend.setText("Vắng mặt");
//                holder.txtAttend.setTextColor(Color.RED);
//            }
//        } else {
//            if (schedule.getAttendance() == null) {
//                holder.txtAttend.setText("N/A");
//            } else if (schedule.getAttendance().equals("true")) {
//                holder.txtAttend.setText("Có mặt");
//                holder.txtAttend.setTextColor(Color.GREEN);
//            } else {
//                holder.txtAttend.setText("Vắng mặt");
//                holder.txtAttend.setTextColor(Color.RED);
//            }
//        }
        return convertView;
    }

    class ViewHolder {
        TextView txtTime, txtSubject, txtTeacher, txtClass;
    }
}
