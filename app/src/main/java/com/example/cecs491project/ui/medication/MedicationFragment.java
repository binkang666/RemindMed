package com.example.cecs491project.ui.medication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class MedicationFragment extends Fragment implements MedicationAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    MedicationAdapter medicationAdapter;
    FirebaseFirestore db;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_medication, container, false);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener(){
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query query = db.collection("User Database")
                .document(uid).collection("Medication");

        FirestoreRecyclerOptions<Medications> options = new FirestoreRecyclerOptions.Builder<Medications>()
                .setQuery(query,Medications.class)
                .build();

        medicationAdapter = new MedicationAdapter(options);

        recyclerView = view.findViewById(R.id.med_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(medicationAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                medicationAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        medicationAdapter.setOnItemClickListener(new MedicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                Medications medications = documentSnapshot.toObject(Medications.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(getContext(), "Edit Function In Development", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        medicationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        medicationAdapter.stopListening();
    }

    public void launchAddOrEditMedication(View v)
    {
        Intent i = new Intent(getContext(), AddMedication.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {

    }
}