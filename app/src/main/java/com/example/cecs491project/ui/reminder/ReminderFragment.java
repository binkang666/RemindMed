package com.example.cecs491project.ui.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.example.cecs491project.databinding.FragmentReminderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


public class ReminderFragment extends Fragment {
    private ArrayList<Reminder> remindersArrayList;
    private Button save;
    private FragmentReminderBinding binding;
    private RecyclerView mReminderList;
    private FirebaseFirestore mFirestore;


    protected void OnCreate(Bundle savedInstanceState){
        //mReminderList = findViewByI;

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {





        return inflater.inflate(R.layout.fragment_reminder,container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}