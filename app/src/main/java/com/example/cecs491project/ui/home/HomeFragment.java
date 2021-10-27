package com.example.cecs491project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.Medications;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.type.DateTime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class HomeFragment extends Fragment {

    RecyclerView overview;
    overviewAdapter overviewAdapter;
    FirebaseFirestore db;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        EventChangeListener();
        return view;
    }

    private void EventChangeListener(){
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query query;
        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            query = db.collection("Anonymous User Database")
                    .document(uid).collection("Medication");
        }else{
            query = db.collection("User Database")
                    .document(uid).collection("Medication");
        }

        FirestoreRecyclerOptions<Medications> options = new FirestoreRecyclerOptions.Builder<Medications>()
                .setQuery(query,Medications.class)
                .build();

        overviewAdapter = new overviewAdapter(options);

        overview = view.findViewById(R.id.UpcomingView);
        overview.setHasFixedSize(true);
        overview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        overview.setAdapter(overviewAdapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        overviewAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        overviewAdapter.stopListening();
    }

}