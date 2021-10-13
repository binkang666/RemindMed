package com.example.cecs491project.ui.medication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class MedicationFragment extends Fragment {

    RecyclerView recyclerView;
    MedicationAdapter medicationAdapter;
    FirebaseFirestore db;
    View view;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_medication, container, false);

        progressBar = view.findViewById(R.id.progressbar);

        EventChangeListener();
        return view;
    }

    private void EventChangeListener(){
        CountDownLatch done = new CountDownLatch(1);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Are you sure you want to delete this medication?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        medicationAdapter.deleteItem(viewHolder.getAdapterPosition());
                        Toast.makeText(getContext(), "Medication Deleted", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        medicationAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                alertDialog.create().show();
            }
        }).attachToRecyclerView(recyclerView);
        medicationAdapter.setOnItemClickListener(new MedicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                String docID = documentSnapshot.getId();
                Map<String, Object> s = documentSnapshot.getData();

                String name = (String) s.get("medicationName");
                int pillCount = Math.toIntExact(Long.parseLong(String.valueOf(s.get("pillCount"))));
                int refillCount = Math.toIntExact(Long.parseLong(String.valueOf(s.get("refillCount"))));
                double dosage = (double) (s.get("dosage"));
                String note = (String) s.get("note");
                String cate = (String) s.get("categories");

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("docID", docID);
                bundle.putString("note", note);
                bundle.putString("cate", cate);
                bundle.putInt("count", pillCount);
                bundle.putInt("refill", refillCount);
                bundle.putDouble("dos", dosage);

                Intent i = new Intent(getContext(), MedicationDetails.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                progressBar.setVisibility(view.GONE);
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


}