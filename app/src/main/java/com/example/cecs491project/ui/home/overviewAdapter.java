package com.example.cecs491project.ui.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.Medications;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class overviewAdapter extends FirestoreRecyclerAdapter<Medications, overviewAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public overviewAdapter(@NonNull FirestoreRecyclerOptions<Medications> medications) {
        super(medications);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Medications model) {

        holder.med_name.setText(model.getMedicationName());
        holder.med_type.setText(model.getCategories());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_overview_home,
                parent,false);
        return new ViewHolder(v);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView med_name;
        TextView med_type;
//        TextView timePart;
//        TextView AMPM;
//        TextView timesLeft;

        public ViewHolder(View itemView) {
            super(itemView);
            med_name = itemView.findViewById(R.id.medicineName);
            med_type = itemView.findViewById(R.id.medicineType);
//            timePart = itemView.findViewById(R.id.timeToTake);
//            AMPM = itemView.findViewById(R.id.amOrPm);
//            timesLeft =itemView.findViewById(R.id.timeLeft);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(getSnapshots().getSnapshot(pos), pos);
                }

            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
