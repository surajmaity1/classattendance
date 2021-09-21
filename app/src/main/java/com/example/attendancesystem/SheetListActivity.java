package com.example.attendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.attendancesystem.ui.DatabaseHelper;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    ListView sheetList;
    ArrayAdapter adapter;
    ArrayList<String> listItems = new ArrayList<>();
    Long cid;
    ImageButton backbtn,savebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        //back button functionality
        backbtn = findViewById(R.id.backButton);
        backbtn.setOnClickListener(v->onBackPressed());

        // hide save button
        savebtn = findViewById(R.id.iconSave);
        savebtn.setVisibility(View.INVISIBLE);


        cid = getIntent().getLongExtra("cid",-1);
        loadListItem();
        sheetList = findViewById(R.id.sheetList);
        adapter = new ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));
    }

    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");

        Intent intent = new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("month",listItems.get(position));
        startActivity(intent);
    }

    private void loadListItem() {
        Cursor cursor = new DatabaseHelper(this).getDistinctMonths(cid);

        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_KEY));
            listItems.add(date.substring(3));
        }
    }
}