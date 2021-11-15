package com.example.cecs491project.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.MedicationAdapter;
import com.example.cecs491project.ui.medication.MedicationDetails;
import com.example.cecs491project.ui.medication.Medications;
import com.example.cecs491project.ui.notifications.Notification;
import com.example.cecs491project.ui.notifications.notificationAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class TypeActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView med_type_list;
    MedicationAdapter medicationAdapter;
    TextView desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        String newString, newString2;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
                newString2 = null;
            } else {
                newString= extras.getString("Title");
                newString2 = extras.getString("Desc");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("Title");
            newString2= (String) savedInstanceState.getSerializable("Desc");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(newString);
        desc = findViewById(R.id.desc_med);
        desc.setText(newString2);

        EventChangeListener(newString);

    }

    private void EventChangeListener(String typeName){
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query query;
        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            query = db.collection("Anonymous User Database")
                    .document(uid).collection("Medication").whereEqualTo("categories", typeName);
        }else{
            query = db.collection("User Database")
                    .document(uid).collection("Medication").whereEqualTo("categories", typeName);
        }

        FirestoreRecyclerOptions<Medications> options = new FirestoreRecyclerOptions.Builder<Medications>()
                .setQuery(query,Medications.class)
                .build();

        medicationAdapter = new MedicationAdapter(options);
        med_type_list = findViewById(R.id.type_list);
        med_type_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        med_type_list.setAdapter(medicationAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                alertDialog.setMessage("Are you sure you want to delete this medication?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        medicationAdapter.deleteItem(viewHolder.getAdapterPosition());
                        Toast.makeText(getApplicationContext(), "Medication Deleted", Toast.LENGTH_LONG).show();
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
        }).attachToRecyclerView(med_type_list);
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

                Intent i = new Intent(getApplicationContext(), MedicationDetails.class);
                i.putExtras(bundle);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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



    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        return true;
    }
}