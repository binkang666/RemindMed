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
import android.widget.Toast;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.EditMedication;
import com.example.cecs491project.ui.medication.MedicationDetails;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Objects;

public class ReminderDetails extends AppCompatActivity {

    TextView rem_name, rem_med, end_date, start_date, rem_time, rem_note, rem_day;

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
        String name = bundle.getString("remName");
        String medName = bundle.getString("medName");
        String startDate = bundle.getString("startD");
        String endDate = bundle.getString("endD");
        String remTime = bundle.getString("medTime");
        String note = bundle.getString("note");
        ArrayList<String> days = bundle.getStringArrayList("days");

        collapse.setTitle(name);

        rem_med = findViewById(R.id.medication_name);
        start_date = findViewById(R.id.startDate);
        end_date = findViewById(R.id.endDate);
        rem_time = findViewById(R.id.timeToTake);
        rem_note = findViewById(R.id.note);
        rem_day = findViewById(R.id.days);

        rem_med.setText(medName);
        start_date.setText(startDate);
        end_date.setText(endDate);
        rem_time.setText(remTime);
        rem_note.setText(note);
        StringBuilder allDays = new StringBuilder();
        for (String s : days){
            allDays.append(s).append("\n");
        }
        rem_day.setText(allDays.toString());


        b = new Bundle();
        b.putString("remName", name);
        b.putString("medName", medName);
        b.putString("startD", startDate);
        b.putString("endD", endDate);
        b.putString("note", note);
        b.putString("medTime",remTime);
        b.putStringArrayList("days", days);

    }
    public void editReminder(View view) {
        Intent intent = new Intent(ReminderDetails.this, editReminderActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        return true;
    }

}
