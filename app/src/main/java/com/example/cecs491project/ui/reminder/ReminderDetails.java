package com.example.cecs491project.ui.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.EditMedication;
import com.example.cecs491project.ui.medication.MedicationDetails;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

public class ReminderDetails extends AppCompatActivity {

    TextView rem_name, rem_med, end_date, start_date, rem_time, rem_note;

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapse = findViewById(R.id.collapse);
        collapse.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapse.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapse.getBackground().setAlpha(175);


        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String medName = bundle.getString("med");
        String startDate = bundle.getString("start");
        String endDate = bundle.getString("end");
        String remTime = bundle.getString("time");
        String note = bundle.getString("note");


    }



}
