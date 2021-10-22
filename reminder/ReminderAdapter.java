package com.example.cecs491project.ui.reminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.sql.Time;

public class ReminderAdapter extends FirestoreRecyclerAdapter<Reminder, ReminderAdapter.ViewHolder> {

    private ReminderAdapter.OnItemClickListener listener;

    public ReminderAdapter(@NonNull FirestoreRecyclerOptions<Reminder> reminders) {
        super(reminders);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position, @NonNull Reminder model) {

        holder.reminderName.setText(model.getReminderName());
        holder.medName.setText(model.getMedicationName());
        String time = model.getTime();
        holder.time.setText(time);

    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_details,
                parent,false);
        ReminderAdapter.ViewHolder viewHolder = new ReminderAdapter.ViewHolder(v);
        return viewHolder;
    }

    public void deleteItem(int pos){
        getSnapshots().getSnapshot(pos).getReference().delete();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView reminderName;
        TextView medName;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            reminderName = itemView.findViewById(R.id.reminder_name);
            medName = itemView.findViewById(R.id.med_name);
            time = itemView.findViewById(R.id.rem_time);

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
    public void setOnItemClickListener(ReminderAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
