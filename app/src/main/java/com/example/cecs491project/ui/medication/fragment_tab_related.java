package com.example.cecs491project.ui.medication;

import android.os.Bundle;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class fragment_tab_related extends Fragment {

    SimpleRelatedReminderAdpater related_adapter;
    RecyclerView related_reminder;
    FirebaseFirestore db;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_tab_related, container, false);
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");
        EventChangeListener(name);
        return v;

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

        related_reminder = v.findViewById(R.id.related_reminder);
        related_reminder.setHasFixedSize(true);
        related_reminder.setLayoutManager(new LinearLayoutManager(getContext()));
        related_reminder.setAdapter(related_adapter);
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