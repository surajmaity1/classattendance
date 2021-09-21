package com.example.attendancesystem.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.attendancesystem.R;

import java.util.ArrayList;

public class AttendanceAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StudentItem> items;
    private TextView frameNameTxt,frameRollTxt;

    public AttendanceAdapter(Context context,ArrayList<StudentItem> items) {
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }
        frameNameTxt = convertView.findViewById(R.id.frameNameTxt);
        frameRollTxt = convertView.findViewById(R.id.frameRollTxt);

        frameNameTxt.setText(items.get(position).getName());
        frameRollTxt.setText(items.get(position).getRoll()+"");
        return convertView;
    }
}
