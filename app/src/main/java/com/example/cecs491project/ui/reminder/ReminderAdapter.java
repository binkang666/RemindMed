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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderAdapter extends FirestoreRecyclerAdapter<Reminder, ReminderAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public ReminderAdapter(@NonNull FirestoreRecyclerOptions<Reminder> reminders) {
        super(reminders);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Reminder model) {

        holder.reminderName.setText(model.getReminderName());
        holder.medicationName.setText(model.getMedicationName());
        holder.time.setText(model.getTime());

        holder.startDate.setText(model.getStartDate());
        holder.endDate.setText(model.getEndDate());

        String startDate, endDate;
        startDate = model.getStartDate();
        endDate = model.getEndDate();
        try {
            Date start=new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
            Date end=new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
            int days = (int) ((end.getTime() - start.getTime())/ (1000*60*60*24));
            holder.daysToCom.setText(String.valueOf(days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.AMPM.setText(model.getAMPM());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_details,
                parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void deleteItem(int pos){
        getSnapshots().getSnapshot(pos).getReference().delete();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView reminderName;
        TextView medicationName;
        TextView time;
        TextView startDate;
        TextView endDate;
        TextView daysToCom;
        TextView AMPM;

        public ViewHolder(View itemView) {
            super(itemView);
            reminderName = itemView.findViewById(R.id.reminder_name);
            medicationName = itemView.findViewById(R.id.medication_name);
            time = itemView.findViewById(R.id.timeToTake);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            daysToCom = itemView.findViewById(R.id.daysToCom);
            AMPM = itemView.findViewById(R.id.amOrPm);

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
