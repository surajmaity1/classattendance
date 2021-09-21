package com.example.attendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancesystem.ui.AttendanceAdapter;
import com.example.attendancesystem.ui.DatabaseHelper;
import com.example.attendancesystem.ui.MyCalender;
import com.example.attendancesystem.ui.StudentItem;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {


    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private ArrayList<StudentItem> stlist = new ArrayList<>();
    private AttendanceAdapter adapter;
    static DatabaseHelper dbhelper;
    static long cid;
    static String className,subjectName;
    static int position;
    Toolbar toolbar;
    MyCalender calender = new MyCalender();
    TextView subTitle;
    int pos;
    String uid,fire_cid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);


        dbhelper = new DatabaseHelper(this);
        Intent i = getIntent();
        cid = i.getLongExtra("cid",-1);
        className = i.getStringExtra("className");
        subjectName = i.getStringExtra("subjectName");
        position = i.getIntExtra("position",-1);
        uid = i.getStringExtra("uid");
        fire_cid = i.getStringExtra("fire_cid");


        subTitle = findViewById(R.id.subtitle_toolbar);


        pos = 0;

        setToolbar();
        loadData();

        adapter = new AttendanceAdapter(this,studentItems);



    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }
    void loadData() {
        Cursor cursor = dbhelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()) {
            long sid = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_NAME_KEY));

            studentItems.add(new StudentItem(sid,name,roll));
            stlist.add(new StudentItem(sid,name,roll));
        }
        cursor.close();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title_toolbar);
        TextView subTitle = findViewById(R.id.subtitle_toolbar);
        ImageButton back = findViewById(R.id.backButton);
        ImageButton save = findViewById(R.id.iconSave);

        save.setOnClickListener(v->saveStatus());

        title.setText(className);
        subTitle.setText(subjectName+" | "+calender.getDate());
        back.setOnClickListener(v->onBackPressed());

        toolbar.inflateMenu(R.menu.attendance_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.student_list) {
            openStudentActivty();
        }
        else if (menuItem.getItemId() == R.id.show_calender) {
            showCalender();
        }


        return true;
    }

    private void openStudentActivty() {
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra("className",className);
        intent.putExtra("subjectName",subjectName);
        intent.putExtra("position",position);
        intent.putExtra("cid",cid);
        intent.putExtra("uid",uid);
        intent.putExtra("fire_cid",fire_cid);

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

    private void saveStatus() {
        for (StudentItem studentItem : stlist) {
            String status = studentItem.getStatus();
            if (status != "P") status = "A";
            long value = dbhelper.addStatus(studentItem.getSid(),cid,calender.getDate(),status);
//            Log.e("pritom","status value "+value);
            if (value == -1) {
                dbhelper.updateStatus(studentItem.getSid(),cid,calender.getDate(),status);
            }
        }
        makeToast(AttendanceActivity.this,"Attendance saved");         // To add
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