package com.example.cecs491project.ui.medication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.example.cecs491project.ui.reminder.Reminder;
import com.example.cecs491project.ui.reminder.ReminderAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class MedicationDetails extends AppCompatActivity {

    TextView tx_name, tx_cate, tx_count, tx_refill, tx_dosage, tx_note;

    Bundle b;

    SimpleRelatedReminderAdpater related_adapter;
    RecyclerView related_reminder;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_details);

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

        EventChangeListener(name);
    }

    public void EventChangeListener(String medName){
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query query;
        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            query = db.collection("Anonymous User Database")
                    .document(uid).collection("Reminder").whereEqualTo("medicationName", medName);

        }else{
            query = db.collection("User Database")
                    .document(uid).collection("Reminder").whereEqualTo("medicationName", medName);
        }

        FirestoreRecyclerOptions<Reminder> options = new FirestoreRecyclerOptions.Builder<Reminder>()
                .setQuery(query,Reminder.class)
                .build();
        related_adapter = new SimpleRelatedReminderAdpater(options);

        related_reminder = findViewById(R.id.related_reminder);
        related_reminder.setHasFixedSize(true);
        related_reminder.setLayoutManager(new LinearLayoutManager(this));
        related_reminder.setAdapter(related_adapter);
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
    @Override
    public void onStart() {
        super.onStart();
        related_adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        related_adapter.stopListening();
    }

}
