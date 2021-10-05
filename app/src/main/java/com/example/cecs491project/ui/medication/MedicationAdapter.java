package com.example.cecs491project.ui.medication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {


    Context context;
    ArrayList<Medications> medicationsArrayList;

    public MedicationAdapter(Context context, ArrayList<Medications> medicationsArrayList) {
        this.context = context;
        this.medicationsArrayList = medicationsArrayList;
    }

    @NonNull
    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication_details, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.ViewHolder holder, int position) {

        Medications medications = medicationsArrayList.get(position);

        holder.med_name.setText(medications.getMedicationName());
        holder.med_type.setText(medications.getCategories());
    }


    @Override
    public int getItemCount() {
        return medicationsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView med_name;
        private final TextView med_type;
        private final ImageView categories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categories = itemView.findViewById(R.id.categories_pics);
            med_name = itemView.findViewById(R.id.medicineName);
            med_type = itemView.findViewById(R.id.medicineType);
        }
    }
}
