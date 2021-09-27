package com.example.cecs491project.ui.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cecs491project.R;

public class ReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

    }


    public void addReminder(View view) {
        Intent i = new Intent(ReminderActivity.this, addReminderActivity.class);
        startActivity(i);
    }
}