package com.example.cecs491project.ui.notifications;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;
import java.util.Objects;

public class NotificationsFragment extends Fragment {
    View view;
    notificationAdapter notificationAdapter;
    RecyclerView notification_view;
    FirebaseFirestore db;
    View v;
    Button save;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        v = inflater.inflate(R.layout.dialog_med_taken, container, false);
        save = v.findViewById(R.id.update);
        EventChangeListener();
        return view;
    }

    public void EventChangeListener(){
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query query;

        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            query = db.collection("Anonymous User Database")
                    .document(uid).collection("Notification").orderBy("");

        }else{
            query = db.collection("User Database")
                    .document(uid).collection("Notification");
        }

        FirestoreRecyclerOptions<Notification> options = new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query,Notification.class)
                .build();
        notificationAdapter = new notificationAdapter(options);

        notification_view = view.findViewById(R.id.notification_list);
        notification_view.setHasFixedSize(true);
        notification_view.setLayoutManager(new LinearLayoutManager(getContext()));
        notification_view.setAdapter(notificationAdapter);
        notificationAdapter.setOnItemClickListener(new notificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Is the Medicine Taken?");
                alertDialog.setCancelable(false);
                Map<String, Object> s = documentSnapshot.getData();
                String reminderName = (String) s.get("reminderName");
                String name = (String) s.get("medicationName");
                String time = (String) s.get("time");
                int takenOrNot = Math.toIntExact(Long.parseLong(String.valueOf(s.get("takenOrNot"))));
                Log.d("TESTING", "NOT YET CLICKED HERE");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference noti = null;
                        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
                            noti = db.collection("Anonymous User Database")
                                    .document(userID).collection("Notification").document(reminderName);
                        }
                        else{
                            noti = db.collection("User Database")
                                    .document(userID).collection("Notification").document(reminderName);
                        }
                        Notification notification = new Notification(reminderName, name, time, takenOrNot+1);
                        noti.set(notification);
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.create().show();

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        notificationAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }

}