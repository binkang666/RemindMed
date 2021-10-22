package com.example.cecs491project.ui.medication;

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

        rem_name = findViewById(R.id.et_cate);
        rem_med = findViewById(R.id.et_note);
        start_date = findViewById(R.id.et_count);
        end_date = findViewById(R.id.et_dosage);
        rem_time = findViewById(R.id.et_refill);
        rem_note = findViewById(R.id)

        collapse.setTitle(name);
        tx_cate.setText(cate);
        tx_note.setText(note);
        tx_count.setText(String.valueOf(count));
        tx_refill.setText(String.valueOf(refill));
        tx_dosage.setText(String.valueOf(dos));
        System.out.println(count + "  " + refill +"   "+  dos +"  check here");

        b = new Bundle();
        b.putString("name", name);
        b.putString("note", note);
        b.putString("cate", cate);
        b.putInt("count", count);
        b.putInt("refill", refill);
        b.putDouble("dos", dos);
    }

    public void editMed(View view) {
        Intent intent = new Intent(MedicationDetails.this, EditMedication.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
