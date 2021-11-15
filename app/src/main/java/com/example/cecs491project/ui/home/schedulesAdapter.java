package com.example.cecs491project.ui.home;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.reminder.Reminder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class schedulesAdapter extends FirestoreRecyclerAdapter<Reminder, schedulesAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public schedulesAdapter(@NonNull FirestoreRecyclerOptions<Reminder> reminders) {
        super(reminders);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Reminder model) {

        holder.rem_name.setText(model.getReminderName());
        holder.med_name.setText(model.getMedicationName());
        holder.time.setText(model.getTime());
        LocalTime localTime = null;
        Date d1 = null, d2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
            try {
                d1 = format.parse(model.getTime());
                d2 = format.parse(format.format(date));
                long diff = Math.abs(d2.getTime() - d1.getTime());
                long diffMinutes = (diff / (60 * 1000));
                long hours = diffMinutes / 60;
                long minutes = diffMinutes % 60;
                holder.time_left.setText(String.valueOf(hours));
                holder.mins_left.setText(String.valueOf(minutes));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String endDate = model.getEndDate();
        String curr = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                curr = localDate.format(formatter);
            }
            Date start=new SimpleDateFormat("dd/MM/yyyy").parse(curr);
            Date end=new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
            int days = (int) ((end.getTime() - start.getTime())/ (1000*60*60*24));
            if(days < 0) {
                holder.daysToCom.setText(String.valueOf(0));
            }else holder.daysToCom.setText(String.valueOf(days));

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedules,
                parent,false);
        return new ViewHolder(v);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView rem_name;
        TextView med_name;
        TextView time;
        TextView time_left;
        TextView mins_left;
        TextView daysToCom;


        public ViewHolder(View itemView) {
            super(itemView);
            rem_name = itemView.findViewById(R.id.ReminderName);
            med_name = itemView.findViewById(R.id.MedicineName);
            time = itemView.findViewById(R.id.takeTime);
            time_left = itemView.findViewById(R.id.timeLeft);
            mins_left =itemView.findViewById(R.id.minsLeft);
            daysToCom = itemView.findViewById(R.id.daysleft);

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
