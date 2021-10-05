package com.example.cecs491project.ui.medication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.example.cecs491project.databinding.FragmentMedicationBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicationFragment extends Fragment {

    private FragmentMedicationBinding binding;

    private ArrayList<Medications> medicationsArrayList;
    RecyclerView recyclerView;
    MedicationAdapter medicationAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_medication, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.med_list);
        db = FirebaseFirestore.getInstance();
        medicationsArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        medicationAdapter = new MedicationAdapter(container.getContext(), medicationsArrayList);
        recyclerView.setAdapter(medicationAdapter);

        EventChangeListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void EventChangeListener(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("User Database")
                .document(uid).collection("Medication").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        medicationsArrayList.add(dc.getDocument().toObject(Medications.class));
                    }
                    medicationAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }


    public void launchAddOrEditMedication(View v)
    {
        Intent i = new Intent(getContext(), AddOrEditMedication.class);
        startActivity(i);
    }
}