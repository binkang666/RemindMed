package com.example.cecs491project.ui.medication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
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
    TabLayout tabLayout;
    ViewPager viewPager;

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

        b = new Bundle();
        b.putString("name", name);
        b.putString("note", note);
        b.putString("cate", cate);
        b.putInt("count", count);
        b.putInt("refill", refill);
        b.putDouble("dos", dos);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPage);

        tabLayout.setupWithViewPager(viewPager);
        fragment_tab_details fragment_tab_details = new fragment_tab_details();
        fragment_tab_related fragment_tab_related = new fragment_tab_related();
        fragment_tab_details.setArguments(b);
        fragment_tab_related.setArguments(b);
        tabAdapter tabAdapter = new tabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabAdapter.addFragment(fragment_tab_details, "Details");
        tabAdapter.addFragment(fragment_tab_related, "Related");
        viewPager.setAdapter(tabAdapter);


    }



    public void editMed(View view) {
        Intent intent = new Intent(MedicationDetails.this, EditMedication.class);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        return true;
    }


}
