package com.example.cecs491project.ui.reminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.AddMedication;
import com.example.cecs491project.ui.medication.MedicationAdapter;
import com.example.cecs491project.ui.medication.MedicationDetails;
import com.example.cecs491project.ui.medication.Medications;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;


public class ReminderFragment extends Fragment {
    View ReminderView;
    RecyclerView myReminderList;
    FirebaseFirestore db;
    ReminderAdapter reminderAdapter;
    ProgressBar progressBar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ReminderView = inflater.inflate(R.layout.fragment_reminder, container, false);

        progressBar = ReminderView.findViewById(R.id.progressbar);

        EventChangeListener();
        return ReminderView;
    }

    private void EventChangeListener(){
        CountDownLatch done = new CountDownLatch(1);
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query query;
        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            query = db.collection("Anonymous User Database")
                    .document(uid).collection("Reminder");
        }else{
            query = db.collection("User Database")
                    .document(uid).collection("Reminder");
        }

        FirestoreRecyclerOptions<Reminder> options = new FirestoreRecyclerOptions.Builder<Reminder>()
                .setQuery(query,Reminder.class)
                .build();

        reminderAdapter = new ReminderAdapter(options);

        myReminderList = ReminderView.findViewById(R.id.reminder_list);
        myReminderList.setHasFixedSize(true);
        myReminderList.setLayoutManager(new LinearLayoutManager(getContext()));
        myReminderList.setAdapter(reminderAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Are you sure you want to delete this reminder?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reminderAdapter.deleteItem(viewHolder.getAdapterPosition());
                        Toast.makeText(getContext(), "Medication Deleted", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reminderAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                alertDialog.create().show();
            }
        }).attachToRecyclerView(myReminderList);
        reminderAdapter.setOnItemClickListener(new ReminderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                String docID = documentSnapshot.getId();
                Map<String, Object> s = documentSnapshot.getData();

                String reminderName = (String) s.get("reminderName");
                String name = (String) s.get("medicationName");
                String start = (String) s.get("startDate");
                String end = (String) s.get("endDate");
                String time = (String) s.get("time");
                String note = (String) s.get("note");
                ArrayList<String> day = (ArrayList<String>) s.get("days");

                Bundle bundle = new Bundle();
                bundle.putString("remName", reminderName);
                bundle.putString("medName", name);
                bundle.putString("startD", start);
                bundle.putString("endD", end);
                bundle.putString("docID", docID);
                bundle.putString("note", note);
                bundle.putString("medTime",time);
                bundle.putStringArrayList("days", day);

                Intent i = new Intent(getContext(), ReminderDetails.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                progressBar.setVisibility(ReminderView.GONE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        reminderAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        reminderAdapter.stopListening();
    }



}