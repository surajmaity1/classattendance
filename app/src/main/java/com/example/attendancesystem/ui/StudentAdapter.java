package com.example.attendancesystem.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    ArrayList<StudentItem> studentItems;
    Context context;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context = context;

    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView recyclerRoll;
        TextView recyclerName;
        TextView status;
        CardView cardView;
        public StudentViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            recyclerRoll = itemView.findViewById(R.id.recyclerRoll);
            recyclerName = itemView.findViewById(R.id.recyclerName);
            status = itemView.findViewById(R.id.recyclerStatus);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"Edit");
            menu.add(getAdapterPosition(),1,0,"Delete");
        }
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentViewHolder(itemView,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        if (studentItems.get(position).getStatus() == null) {
            setItemStatus(studentItems,position);
        }
        holder.recyclerRoll.setText(studentItems.get(position).getRoll()+"");
        holder.recyclerName.setText(studentItems.get(position).getName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status = "old";
        status = studentItems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.present)));
        else if (status.equals("A"))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.absent)));
        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white)));


    }
    private void setItemStatus(ArrayList<StudentItem> items,int position) {
        items.get(position).setStatus("");
    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }




}
