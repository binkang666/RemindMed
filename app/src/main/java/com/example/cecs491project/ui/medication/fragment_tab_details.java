package com.example.cecs491project.ui.medication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.reminder.Reminder;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


public class fragment_tab_details extends Fragment {

    TextView tx_name, tx_cate, tx_count, tx_refill, tx_dosage, tx_note;

    Bundle b;

    SimpleRelatedReminderAdpater related_adapter;
    RecyclerView related_reminder;
    FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_details, container, false);

        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");
        String note = bundle.getString("note");
        String cate = bundle.getString("cate");
        int count = bundle.getInt("count");
        int refill = bundle.getInt("refill");
        double dos = bundle.getDouble("dos");

        tx_cate = v.findViewById(R.id.et_cate);
        tx_note = v.findViewById(R.id.et_note);
        tx_count = v.findViewById(R.id.et_count);
        tx_dosage = v.findViewById(R.id.et_dosage);
        tx_refill = v.findViewById(R.id.et_refill);

        tx_cate.setText(cate);
        tx_note.setText(note);
        tx_count.setText(String.valueOf(count));
        tx_refill.setText(String.valueOf(refill));
        tx_dosage.setText(String.valueOf(dos));


        return v;

    }


}