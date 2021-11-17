package com.example.cecs491project.ui.home;


import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.Medications;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class overviewAdapter extends RecyclerView.Adapter<overviewAdapter.ViewHolder> {
    private final ArrayList<String> ItemName;
    private final ArrayList<Integer> ItemImage;
    private final ArrayList<String> ItemDesc;
    private final Context mContext;

    public overviewAdapter(Context context, ArrayList<String> itemname, ArrayList<Integer> image, ArrayList<String> desc) {
        ItemName=itemname;
        mContext = context;
        ItemImage = image;
        ItemDesc =desc;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_overview_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(getmContext())
                .load(ItemImage.get(position))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(holder.imageView);
        holder.itemName.setText(ItemName.get(position));


    }

    @Override
    public int getItemCount() {
        return ItemName.size();
    }

    public Context getmContext() {
        return mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        ImageView imageView;
        TextView itemDesc;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.imageView);
            itemDesc = itemView.findViewById(R.id.desc_med);
        }
    }

}
