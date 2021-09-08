package com.example.cecs491project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    //private String[] localDataSet;
    List<Medications> medicationsList;
    Context context;
    private final OnTodoClickListener onTodoClickListener;


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.

    public RecyclerViewAdapter(String[] dataSet) {
        localDataSet = dataSet;
    }*/

    public RecyclerViewAdapter(List<Medications> medicationsList, Context context, OnTodoClickListener onTodoClickListener) {
        this.medicationsList = medicationsList;
        this.context = context;
        this.onTodoClickListener = onTodoClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.one_line_medication, viewGroup, false);


        return new ViewHolder(view, onTodoClickListener);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Medications medications = medicationsList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tv_medNumber.setText(String.valueOf(medicationsList.get(position).getMedicineNumber()));
        viewHolder.tv_medName.setText(medicationsList.get(position).getMedicationName());
        viewHolder.tv_medPillCount.setText(String.valueOf(medicationsList.get(position).getPillCount()));
        viewHolder.tv_medDosage.setText(String.valueOf(medicationsList.get(position).getDosage()));
        viewHolder.tv_medRefilCount.setText(String.valueOf(medicationsList.get(position).getRefillCount()));
//        viewHolder.checkBox.setTag(medicationsList.get(position));
//        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox checkBox = (CheckBox) v;
//
//                if (checkBox.isChecked())
//                {
//                    int newPosition = viewHolder.getAdapterPosition();
//                    medicationsList.remove(position);
//                    notifyItemRemoved(newPosition);
//                    notifyItemRangeChanged(newPosition, medicationsList.size());
//
//
//
//                }
//                Toast.makeText(context.getApplicationContext(), "Medication deleted", Toast.LENGTH_SHORT).show();
//            }
//        });
//        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick (View v)
//            {
//                Intent i = new Intent(context, AddOrEditMedication.class);
//                i.putExtra("id", medicationsList.get(position).getMedicineNumber());
//                i.putExtra("name", medicationsList.get(position).getMedicationName());
//                i.putExtra("pillCount", medicationsList.get(position).getPillCount());
//                i.putExtra("dosage", medicationsList.get(position).getDosage());
//                i.putExtra("refill", medicationsList.get(position).getRefillCount());
//                context.startActivity(i);
//
//            }
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return medicationsList.size();
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_medNumber;
        private final TextView tv_medName;
        private final TextView tv_medPillCount;
        private final TextView tv_medDosage;
        private final TextView tv_medRefilCount;
        private final CheckBox checkBox;
        ConstraintLayout parentLayout;
        OnTodoClickListener onTodoClickListener;

        public ViewHolder(View view, OnTodoClickListener onTodoClickListener) {
            super(view);
            // Define click listener for the ViewHolder's View

            tv_medNumber = (TextView) view.findViewById(R.id.textViewMedNumber);
            tv_medName = (TextView) view.findViewById(R.id.textViewMedName);
            tv_medPillCount = (TextView) view.findViewById(R.id.textViewMedPillCount);
            tv_medDosage = (TextView) view.findViewById(R.id.textViewMedDosage);
            tv_medRefilCount = (TextView) view.findViewById(R.id.textViewMedRefillCount);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            parentLayout = itemView.findViewById(R.id.oneLineMedicationLayout);
            this.onTodoClickListener = onTodoClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //change boolean value
            //clicked=true;
            int id = v.getId();
            if (id == R.id.oneLineMedicationLayout)
            {
                onTodoClickListener.onTodoClick(getAdapterPosition());
            }
            else if (id == R.id.checkBox)
            {
                // onTodoClickListener.onTodoCheck(viewholder, getAdapterPosition());
            }

        }

    }
    public interface OnTodoClickListener {
        void onTodoClick(int position);

    }

}

