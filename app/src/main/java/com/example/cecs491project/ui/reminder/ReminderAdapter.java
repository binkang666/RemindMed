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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        String timePart = model.getTime().replaceAll("[^0-9:]", "");
        holder.time.setText(timePart);

        holder.startDate.setText(model.getStartDate());
        holder.endDate.setText(model.getEndDate());

        String endDate = model.getEndDate();
        String curr = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                curr = localDate.format(formatter);
                System.out.println(curr);
            }
            Date start=new SimpleDateFormat("dd/MM/yyyy").parse(curr);
            Date end=new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
            int days = (int) ((end.getTime() - start.getTime())/ (1000*60*60*24));
            if(days < 0) {
                holder.daysToCom.setText(String.valueOf(0));
            }else holder.daysToCom.setText(String.valueOf(days));
            System.out.println(end + " - " + start + " = " + days);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String AMPM = model.getTime().replaceAll("[^a-zA-Z]+", "");
        holder.AMPM.setText(AMPM);

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
