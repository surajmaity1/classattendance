package com.example.attendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.attendancesystem.ui.DatabaseHelper;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);



        showTable();
    }

    private void showTable() {
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String month = getIntent().getStringExtra("month");

        int DAY_IN_MONTH = getDayInMonth(month);

        //row setup
        int rowSize = idArray.length+1;

        TableRow[] rows = new TableRow[rowSize];
        TextView[] roll_tvs = new TextView[rowSize];
        TextView[] name_tvs = new TextView[rowSize];
        TextView[][] status_tvs = new TextView[rowSize][DAY_IN_MONTH+1];


        for (int i = 0;i < rowSize;i++) {
            roll_tvs[i] = new TextView(this);
            name_tvs[i] = new TextView(this);
            for (int j = 1;j <= DAY_IN_MONTH;j++) {
                status_tvs[i][j] = new TextView(this);
            }
        }

        //header

        roll_tvs[0].setText("Roll");
        roll_tvs[0].setTypeface(roll_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setText("Name");
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);
        for (int i = 1;i <= DAY_IN_MONTH;i++) {
            status_tvs[0][i].setText(i+"");
            status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(),Typeface.BOLD);
        }

        for (int i = 1;i < rowSize;i++) {
            roll_tvs[i].setText(rollArray[i-1]+"");
            name_tvs[i].setText(nameArray[i-1]+"");

            for (int j = 1;j <= DAY_IN_MONTH;j++ ) {
                String day = String.valueOf(j);
                if (day.length() == 1) day = "0"+day;
                String date = day+"."+month;
                String status = dbhelper.getStatus(idArray[i-1],date);
                Log.e("pritom","status is "+status);
                status_tvs[i][j].setText(status);
            }
        }

        for (int i = 0;i < rowSize;i++) {
            rows[i] = new TableRow(this);

            if (i%2 == 0) {
                rows[i].setBackgroundColor(Color.parseColor("#CCE5FF"));
            } else {
                rows[i].setBackgroundColor(Color.parseColor("#CCFFFF"));
            }

            roll_tvs[i].setPadding(16,16,16,16);
            name_tvs[i].setPadding(16,16,16,16);

            rows[i].addView(roll_tvs[i]);
            rows[i].addView(name_tvs[i]);

            for (int j = 1;j <= DAY_IN_MONTH;j++) {
                status_tvs[i][j].setPadding(16,16,16,16);
                rows[i].addView(status_tvs[i][j]);
            }

            tableLayout.addView(rows[i]);
        }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);
    }

    private int getDayInMonth(String month) {
        int monthIndex = Integer.valueOf(month.substring(0,1));
        int year = Integer.valueOf(month.substring(4));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}