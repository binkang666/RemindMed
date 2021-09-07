package com.example.cecs491project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MedicationsActivity extends AppCompatActivity implements RecyclerViewAdapter.OnTodoClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    MyMedication myMedication = (MyMedication) this.getApplication();
    List<Medications> medicationsList;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        medicationsList = myMedication.getMedicationsList();

        recyclerView = findViewById(R.id.rv_medications);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapter(medicationsList, this, this);
        recyclerView.setAdapter(mAdapter);
        Toast.makeText(this, "Medications count = " + medicationsList.size(), Toast.LENGTH_LONG).show();
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            //Bug when sorting. Deletes all medications, or edits wrong one
//            case R.id.menu_sortMedicineName:
//                //sort alphabetically
//                Collections.sort(medicationsList, Medications.MedicationNameAZCompare);
//                Toast.makeText(getApplicationContext(), "Sort alphabetically", Toast.LENGTH_SHORT).show();
//                mAdapter.notifyDataSetChanged(); //Let adapter know we changed the order
//                return true;
//
//            case R.id.menu_sortMedicineNameReverse:
//                Collections.sort(medicationsList, Medications.MedicationNameZACompare);
//                Toast.makeText(getApplicationContext(), "Sort alphabetically REVERSED", Toast.LENGTH_SHORT).show();
//                mAdapter.notifyDataSetChanged(); //Let adapter know we changed the order
//                return true;
//
//            case R.id.menu_sortWhenCreated:
//                Collections.sort(medicationsList, Medications.MedicationNameDateCreatedCompare);
//                Toast.makeText(getApplicationContext(), "Sort When Date Created", Toast.LENGTH_SHORT).show();
//                mAdapter.notifyDataSetChanged(); //Let adapter know we changed the order
//                return true;
            case R.id.navigation_home:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }


    public void launchAddOrEditMedication(View v)
    {
        Intent i = new Intent(this, AddOrEditMedication.class);
        startActivity(i);
    }

    @Override
    public void onTodoClick(int position) {
        Intent intent = new Intent(MedicationsActivity.this, AddOrEditMedication.class );
        intent.putExtra("id", medicationsList.get(position).getMedicineNumber());
        startActivity(intent);
    }

    //Swipe right to delete
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {
            medicationsList.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();
            Toast.makeText(MedicationsActivity.this, "Medication delete", Toast.LENGTH_SHORT).show();
        }

    };
}