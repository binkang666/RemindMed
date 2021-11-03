package com.example.cecs491project.ui.notifications;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.Medications;
import com.example.cecs491project.ui.reminder.Reminder;
import com.example.cecs491project.ui.reminder.ReminderAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class notificationAdapter extends FirestoreRecyclerAdapter<Notification, notificationAdapter.ViewHolder> {
    private com.example.cecs491project.ui.notifications.notificationAdapter.OnItemClickListener listener;

    public notificationAdapter(@NonNull FirestoreRecyclerOptions<Notification> notification) {
        super(notification);
    }

    @Override
    protected void onBindViewHolder(@NonNull com.example.cecs491project.ui.notifications.notificationAdapter.ViewHolder holder, int position, @NonNull Notification model) {


        holder.med_name.setText(model.getMedicationName());
        holder.reminderName.setText(model.getReminderName());
        holder.time.setText(model.getTime());


    }

    @NonNull
    @Override
    public com.example.cecs491project.ui.notifications.notificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications,
                parent,false);
        return new com.example.cecs491project.ui.notifications.notificationAdapter.ViewHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView reminderName;
        TextView med_name;
        TextView time;


        public ViewHolder(View itemView) {
            super(itemView);
            reminderName = itemView.findViewById(R.id.reminder_name);
            med_name = itemView.findViewById(R.id.medicineName);
            time = itemView.findViewById(R.id.timeToTake);


            itemView.setOnClickListener(view -> {
                ImageView dot;
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(getSnapshots().getSnapshot(pos), pos);
                    //dot can be deleted if you think its unnecessary
                    dot = view.findViewById(R.id.dot);
                    dot.setVisibility(View.GONE);
                }

            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int pos);
    }

}
