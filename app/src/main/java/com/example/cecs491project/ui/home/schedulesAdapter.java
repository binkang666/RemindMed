package com.example.cecs491project.ui.home;


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

public class schedulesAdapter extends FirestoreRecyclerAdapter<Reminder, schedulesAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public schedulesAdapter(@NonNull FirestoreRecyclerOptions<Reminder> reminders) {
        super(reminders);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Reminder model) {

        holder.rem_name.setText(model.getReminderName());

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
        TextView time;


        public ViewHolder(View itemView) {
            super(itemView);
            rem_name = itemView.findViewById(R.id.ReminderName);
            time = itemView.findViewById(R.id.timeToTake);

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
