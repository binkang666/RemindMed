package com.example.cecs491project.ui.medication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

//TODO: back button somehow not working, need fix
public class MedicationDetails extends AppCompatActivity {

    TextView tx_name, tx_cate, tx_count, tx_refill, tx_dosage, tx_note;

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapse = findViewById(R.id.collapse);
        collapse.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapse.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapse.getBackground().setAlpha(175);



        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String note = bundle.getString("note");
        String cate = bundle.getString("cate");
        int count = bundle.getInt("count");
        int refill = bundle.getInt("refill");
        double dos = bundle.getDouble("dos");

        tx_cate = findViewById(R.id.et_cate);
        tx_note = findViewById(R.id.et_note);
        tx_count = findViewById(R.id.et_count);
        tx_dosage = findViewById(R.id.et_dosage);
        tx_refill = findViewById(R.id.et_refill);

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

    public void goBack(View view) {
        Intent intent = new Intent(MedicationDetails.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
