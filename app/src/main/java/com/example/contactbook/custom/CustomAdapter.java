package com.example.contactbook.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.contactbook.R;
import com.example.contactbook.model.Mark;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    List<Mark> items;
    private Context context;
    private int layout;

    public CustomAdapter(Context context, int layout, List<Mark> items) {
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
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_ds_adapter_diemso, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTenSV = (TextView) convertView.findViewById(R.id.text_subject);
        holder.txtCC = (TextView) convertView.findViewById(R.id.text_mark);
        holder.txtHalf = (TextView) convertView.findViewById(R.id.text_half_mark);
        holder.txtKT = (TextView) convertView.findViewById(R.id.text_feedback);

        Mark diemSo = items.get(position);
        // set text
        holder.txtTenSV.setText(diemSo.getSubjectName());
        holder.txtCC.setText(""+diemSo.getSemesterMark());
        holder.txtHalf.setText(""+diemSo.getHalfMark());
        holder.txtKT.setText(diemSo.getSemesterFeedback());

        return convertView;
    }

    class ViewHolder {
        TextView txtTenSV, txtCC, txtKT, txtHalf;
    }
}
