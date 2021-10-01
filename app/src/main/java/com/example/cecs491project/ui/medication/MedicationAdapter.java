package com.example.cecs491project.ui.medication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            med_name = itemView.findViewById(R.id.medicineName);
            med_type = itemView.findViewById(R.id.medicineType);
        }
    }
}
