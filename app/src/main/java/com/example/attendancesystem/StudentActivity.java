package com.example.attendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancesystem.ui.DatabaseHelper;
import com.example.attendancesystem.ui.MyCalender;
import com.example.attendancesystem.ui.MyDialog;
import com.example.attendancesystem.ui.StudentAdapter;
import com.example.attendancesystem.ui.StudentItem;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    Intent intent;
    String className,subjectName,uid,fire_cid;
    int position;
    RecyclerView recyclerView;
    StudentAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<StudentItem> studentItems = new ArrayList<>();
    DatabaseHelper dbhelper ;
    long cid;
    MyCalender calender;
    TextView subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        calender = new MyCalender();
        intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position",0);
        cid = intent.getLongExtra("cid",-1);
        uid = intent.getStringExtra("uid");
        fire_cid = intent.getStringExtra("fire_cid");
        Log.e("fireid",fire_cid+"");

        dbhelper = new DatabaseHelper(this);


        setToolbar();
        loadData();
        recyclerView = findViewById(R.id.studentRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> changeStatus(position));
        loadStatusData();
    }


    private void loadData() {
        Cursor cursor = dbhelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()) {
            long sid = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid,name,roll));

        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();

        if (status.equals("P")) status = "A";
        else status = "P";

        studentItems.get(position).setStatus(status);
        adapter.notifyDataSetChanged();
        Log.e("pritom",studentItems.get(position).getStatus()+"");
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title_toolbar);
        subTitle = findViewById(R.id.subtitle_toolbar);
        ImageButton back = findViewById(R.id.backButton);
        ImageButton save = findViewById(R.id.iconSave);

        save.setOnClickListener(v->saveStatus());

        title.setText(className);
        subTitle.setText(subjectName+" | "+calender.getDate());
        back.setOnClickListener(v->onBackPressed());

        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems) {
            String status = studentItem.getStatus();
            if (status != "P") status = "A";

            //Sqlite status save
            long value = dbhelper.addStatus(studentItem.getSid(),cid,calender.getDate(),status);
            Log.e("pritom","status value "+value);
            if (value == -1) {
                dbhelper.updateStatus(studentItem.getSid(),cid,calender.getDate(),status);
            }



        }
        Toast.makeText(this,"Attendance saved",Toast.LENGTH_SHORT);           // To add
    }
    private void loadStatusData() {
        for (StudentItem studentItem : studentItems) {
            String status = dbhelper.getStatus(studentItem.getSid(),calender.getDate());
            if (status != null) studentItem.setStatus(status);
            Log.e("pritom","status data "+status);
        }
        adapter.notifyDataSetChanged();
    }
//
    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student) {
            showAddStudentDialog();
        }
        else if (menuItem.getItemId() == R.id.show_calender) {
            showCalender();
        }
        else if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();
        }

        return true;
    }

    private void openSheetList() {
        int n = studentItems.size();
        long[] idArray = new long[n];
        String[] nameArray = new String[n];
        int[] rollArray = new int[n];

        for (int i = 0;i < n;i++) {
            idArray[i] = studentItems.get(i).getSid();
            nameArray[i] = studentItems.get(i).getName();
            rollArray[i] = studentItems.get(i).getRoll();
        }

        Intent intent = new Intent(this,SheetListActivity.class);
        intent.putExtra("cid",cid);
        intent.putExtra("idArray",idArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("rollArray",rollArray);
        startActivity(intent);
    }

    private void showCalender() {

        calender.show(getSupportFragmentManager(),"");
        calender.setOnCalenderClickListener(this::onClalenderClicked);
    }

    private void onClalenderClicked(int year, int month, int day) {
        calender.setDate(year,month,day);
        subTitle.setText(subjectName+" | "+calender.getDate());
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.valueOf(roll_string);

//        Sqlite data change
        long sid = dbhelper.addStudent(cid,roll,name);
        StudentItem studentItem = new StudentItem(sid, name,roll);
        studentItems.add(studentItem);
        Log.e("pritom",studentItem.getName()+studentItem.getRoll());
        adapter.notifyDataSetChanged();


    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0 :
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1 :
                deleteClass(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        //SQLite data change
        dbhelper.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);


    }



    private void deleteClass(int groupId) {
        //SQLite Data delete
        dbhelper.deleteStudent(studentItems.get(groupId).getSid());

        studentItems.remove(groupId);
        adapter.notifyDataSetChanged();
    }

    private String changeDateFormat(String date) {
        String res = "";
        for (int i = 0;i < date.length();i++) {
            if (date.charAt(i) == '.') {
                res += '-';
                continue;
            }
            res += date.charAt(i);
        }
        return res;
    }
}