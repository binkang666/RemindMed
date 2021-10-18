package com.example.cecs491project.ui.medication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class MedicationAdapter extends FirestoreRecyclerAdapter<Medications, MedicationAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public MedicationAdapter(@NonNull FirestoreRecyclerOptions<Medications> medications) {
        super(medications);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Medications model) {

        holder.med_name.setText(model.getMedicationName());
        holder.med_type.setText(model.getCategories());
        String cate = model.getCategories();
        switch (cate) {
            case "Tablet":
                holder.category.setImageResource(R.drawable.tablet);
                break;
            case "Capsule":
                holder.category.setImageResource(R.drawable.capsule);
                break;
            case "Drops":
                holder.category.setImageResource(R.drawable.drops);
                break;
            case "Injection":
                holder.category.setImageResource(R.drawable.injection);
                break;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication_details,
                parent,false);
        return new ViewHolder(v);
    }

    public void deleteItem(int pos){
        getSnapshots().getSnapshot(pos).getReference().delete();

    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView med_name;
        TextView med_type;
        ImageView category;

        public ViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.categories_pics);
            med_name = itemView.findViewById(R.id.medicineName);
            med_type = itemView.findViewById(R.id.medicineType);

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
