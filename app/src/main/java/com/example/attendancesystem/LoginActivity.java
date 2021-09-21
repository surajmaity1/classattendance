package com.example.attendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.example.attendancesystem.ui.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText emailText,passText;
    Button regBtn,logBtn;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.emailTextId);
        passText = findViewById(R.id.passTextId);
        regBtn = findViewById(R.id.regBtn);
        logBtn = findViewById(R.id.logBtn);

        dbhelper = new DatabaseHelper(this);

        logBtn.setOnClickListener(v -> logFunc());
        regBtn.setOnClickListener(v->regFunc());
    }

    private void regFunc() {
        String email = emailText.getText().toString();
        String pass = passText.getText().toString();

        if (!pass.isEmpty()) {
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                 }

        } else {
            passText.setError("Enter password");
        }
    }

    private void logFunc() {
        String email = emailText.getText().toString();
        String pass = passText.getText().toString();

        if (!pass.isEmpty()) {
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                }

        } else {
            passText.setError("Enter password");
        }
    }



    private String changeDateFormat(String date) {
        String res = "";
        for (int i = 0;i < date.length();i++) {
            if (date.charAt(i) == '-') {
                res += '.';
                continue;
            }
            res += date.charAt(i);
        }
        return res;
    }
}