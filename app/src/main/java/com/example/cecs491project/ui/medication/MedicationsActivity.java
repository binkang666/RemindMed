package com.example.cecs491project.ui.medication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicationsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Medications> medicationsArrayList;
    MedicationAdapter medicationAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_medication);

        recyclerView = findViewById(R.id.med_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        medicationsArrayList = new ArrayList<Medications>();
        medicationAdapter = new MedicationAdapter(MedicationsActivity.this, medicationsArrayList);

        recyclerView.setAdapter(medicationAdapter);
        EventChangeListener();

    }
    private void EventChangeListener(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("User Database")
                .document(uid).collection("Medication").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        medicationsArrayList.add(dc.getDocument().toObject(Medications.class));
                    }
                    medicationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void launchAddOrEditMedication(View v)
    {
        Intent i = new Intent(MedicationsActivity.this, AddOrEditMedication.class);
        startActivity(i);
    }
}
