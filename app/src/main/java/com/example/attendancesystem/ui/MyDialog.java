package com.example.attendancesystem.ui;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.attendancesystem.R;

public class MyDialog extends DialogFragment {

    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";

    onClickListener listener;
    int roll;
    String name;
    public MyDialog(int roll, String name) {
        this.roll = roll;
        this.name = name;
    }

    public MyDialog() {

    }

    public interface onClickListener {
        void onClick(String text1,String text2);
    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(CLASS_ADD_DIALOG)) {
            dialog=getAddClassDialog();
        }
        if (getTag().equals(STUDENT_ADD_DIALOG)) {
            dialog=getAddStudentDialog();
        }
        if (getTag().equals(CLASS_UPDATE_DIALOG)) {
            dialog = getUpdateClassDialog();
        }        
        if (getTag().equals(STUDENT_UPDATE_DIALOG)) {
            dialog = getUpdateStudentDialog();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Student");

        EditText rollEdit = view.findViewById(R.id.edt01);
        EditText nameEdit = view.findViewById(R.id.edt02);
        rollEdit.setHint("Enter Roll (Only number)");
        nameEdit.setHint("Enter Student Name");

        Button addClassBtn = view.findViewById(R.id.addClassBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        addClassBtn.setText("Update");
        rollEdit.setText(roll+"");
        rollEdit.setEnabled(false);
        nameEdit.setText(name);

        cancelBtn.setOnClickListener(v->dismiss());
        addClassBtn.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();              // Name and roll swapping mistake
            String roll = rollEdit.getText().toString();
            listener.onClick(roll,name);
            dismiss();
        });

        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Class");

        EditText classEdit = view.findViewById(R.id.edt01);
        EditText subjectEdit = view.findViewById(R.id.edt02);
        classEdit.setHint("Class Name");
        subjectEdit.setHint("Subject Name");

        Button addClassBtn = view.findViewById(R.id.addClassBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        addClassBtn.setText("Update");

        cancelBtn.setOnClickListener(v->dismiss());
        addClassBtn.setOnClickListener(v -> {
            String className = classEdit.getText().toString();
            String subName = subjectEdit.getText().toString();
            listener.onClick(className,subName);
            dismiss();
        });

        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Student");

        EditText rollEdit = view.findViewById(R.id.edt01);
        EditText nameEdit = view.findViewById(R.id.edt02);
        rollEdit.setHint("Enter Roll (Only number)");
        nameEdit.setHint("Enter Student Name");

        Button addClassBtn = view.findViewById(R.id.addClassBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(v->dismiss());
        addClassBtn.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();              // Name and roll swapping mistake
            String roll = rollEdit.getText().toString();
            listener.onClick(roll,name);
            rollEdit.setText(String.valueOf(Integer.parseInt(roll)+1));
            nameEdit.setText("");
        });

        return builder.create();
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add new Class");

        EditText classEdit = view.findViewById(R.id.edt01);
        EditText subjectEdit = view.findViewById(R.id.edt02);
        classEdit.setHint("Enter Class");
        subjectEdit.setHint("Enter Subject");

        Button addClassBtn = view.findViewById(R.id.addClassBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(v->dismiss());
        addClassBtn.setOnClickListener(v -> {
            String className = classEdit.getText().toString();
            String subName = subjectEdit.getText().toString();
            listener.onClick(className,subName);
            dismiss();
        });

        return builder.create();

    }
}
