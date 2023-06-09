package com.example.contactbook.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.contactbook.R;
import com.example.contactbook.model.SortMark;

import java.util.List;

public class FinalMarkAdapter  extends BaseAdapter {
    List<SortMark> items;
    private Context context;
    private int layout;

    public FinalMarkAdapter(Context context, int layout, List<SortMark> items) {
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
        FinalMarkAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new FinalMarkAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custome_ds_final_mark, null);
            convertView.setTag(holder);
        } else {
            holder = (FinalMarkAdapter.ViewHolder) convertView.getTag();
        }
        holder.txtTenSV = (TextView) convertView.findViewById(R.id.text_subject);
        holder.txtCC = (TextView) convertView.findViewById(R.id.text_mark_2);
        holder.txtHalf = (TextView) convertView.findViewById(R.id.text_mark_1);
        holder.txtKT = (TextView) convertView.findViewById(R.id.text_final);
        holder.txtFb = (TextView) convertView.findViewById(R.id.text_danh_gia);

        SortMark diemSo = items.get(position);
        // set text
        holder.txtTenSV.setText(diemSo.getSubjectName());
        holder.txtCC.setText(""+diemSo.getMark2S());
        holder.txtHalf.setText(""+diemSo.getMark1S());
        holder.txtKT.setText(diemSo.getFinalMark());
        holder.txtFb.setText(diemSo.getFeedback());
        return convertView;
    }

    class ViewHolder {
        TextView txtTenSV, txtCC, txtKT, txtHalf, txtFb;
    }
}
