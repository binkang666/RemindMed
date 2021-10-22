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

import com.example.cecs491project.R;
import com.example.cecs491project.databinding.FragmentReminderBinding;
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
    private ArrayList<Reminder> remindersArrayList;
    private Button save;
    private ReminderViewModel reminderViewModel;
    private FragmentReminderBinding binding;
    private View ReminderView;
    private RecyclerView myReminderList;
    private FirebaseFirestore db;
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

        Query query = db.collection("User Database")
                .document(uid).collection("Reminder");

        FirestoreRecyclerOptions<Reminder> options = new FirestoreRecyclerOptions.Builder<Reminder>()
                .setQuery(query,Reminder.class)
                .build();

        reminderAdapter = new ReminderAdapter(options);

        myReminderList = ReminderView.findViewById(R.id.reminder_list);
        myReminderList.setHasFixedSize(true);
        myReminderList.setLayoutManager(new LinearLayoutManager(getContext()));
        myReminderList.setAdapter(reminderAdapter);

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