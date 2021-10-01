package com.example.cecs491project.ui.medication;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cecs491project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddOrEditMedication extends AppCompatActivity {

    EditText et_medName, et_medPillCount, et_medDosage, et_medRefillCount, medicationNote;
    private TextView SAVE;
    private View Card_View ;
    private ImageView ASClose ;

    private Button tablet, capsule, drops, injections;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_medication);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);
        initializePage();

        ASClose.setOnClickListener(v -> AddOrEditMedication.super.onBackPressed());

    }

    private void initializePage() {
        et_medName = findViewById(R.id.editTextMedicationName);
        et_medPillCount = findViewById(R.id.editTextPillCount);
        et_medDosage = findViewById(R.id.editTextDosage);
        et_medRefillCount = findViewById(R.id.editTextRefillCount);
        ASClose = findViewById(R.id.ASClose);
        SAVE = findViewById(R.id.SAVE);
        Card_View = findViewById(R.id.Card_View);
        medicationNote = findViewById(R.id.medication_note);

        tablet = findViewById(R.id.ctg_Tablet);
        capsule = findViewById(R.id.ctg_Capsule);
        drops = findViewById(R.id.ctg_Drops);
        injections = findViewById(R.id.ctg_Injection);

        SAVE.setZ(20);
        Card_View.setZ(2);
        ASClose.setZ(20);
    }

    private void makeSnackBarMessage(String message){
        Snackbar.make(Card_View, message, Snackbar.LENGTH_SHORT).show();
    }

    //TODO： User should be able to add the medication even if some information remain unfinished. (right now, you need to add every information to add)
    //TODO: add picture feature can be deleted if unnecessary.
    private void addMedToDatabase(String categories){
        SAVE.setOnClickListener(view -> {

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


            Map<String, Object> tabletMap = new HashMap<>();
            String medName = et_medName.getText().toString();
            tabletMap.put("Categories",categories);

            int pillCount =  Integer.parseInt(et_medPillCount.getText().toString());
            tabletMap.put("Pill Count", pillCount);
            int dosage =  Integer.parseInt(et_medDosage.getText().toString());
            tabletMap.put("Dosage", dosage);
            int refillCount =  Integer.parseInt(et_medRefillCount.getText().toString());
            tabletMap.put("Refill Count", refillCount);

            String Note = medicationNote.getText().toString();
            tabletMap.put("Medication Note", Note);


            DocumentReference userTablet = db.collection("User Database")
                    .document(userID).collection("Medication").document(medName);

            Medications med = new Medications(medName, categories, pillCount,  dosage, refillCount, Note);

            userTablet.set(med).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        makeSnackBarMessage("Medication added");
                    }
                    else{
                        makeSnackBarMessage("Failed");
                    }
                }
            });
            finish();
        });
    }

    public void onClickCategories( View view ){
        int id = view.getId();
        TransitionManager.beginDelayedTransition((ConstraintLayout)findViewById(R.id.Card_View));
        View SelectCategories = findViewById(R.id.select_categories);
        View Group_Drops = findViewById(R.id.Drop_Group);

        if(id == R.id.ctg_Tablet) {
            if ( SelectCategories.getVisibility() == View.VISIBLE ){

                SelectCategories.setVisibility(View.GONE );
                (findViewById(R.id.ctg_Tablet)).setAlpha((float)0.25);

            }else{
                Group_Drops.setVisibility(View.GONE );
                (findViewById(R.id.ctg_Tablet)).setAlpha(1);
            }
            (findViewById(R.id.ctg_Capsule)).setAlpha((float) 0.25);
            (findViewById(R.id.ctg_Injection)).setAlpha((float)0.25);
            (findViewById(R.id.ctg_Drops)).setAlpha((float)0.25);


            addMedToDatabase("Tablet");

        }
        else if(id == R.id.ctg_Drops){
            if (Group_Drops.getVisibility() == View.GONE ){

                SelectCategories.setVisibility(View.GONE);

                Group_Drops.setVisibility(View.VISIBLE);
                (findViewById(R.id.ctg_Drops)).setAlpha(1);

            }else{

                Group_Drops.setVisibility(View.GONE);
                SelectCategories.setVisibility(View.GONE);

                (findViewById(R.id.ctg_Drops)).setAlpha((float)0.25);

            }
            (findViewById(R.id.ctg_Tablet)).setAlpha((float) 0.25);
            (findViewById(R.id.ctg_Capsule)).setAlpha((float) 0.25);
            (findViewById(R.id.ctg_Injection)).setAlpha((float)0.25);
            addMedToDatabase("Drops");

        }
        else if(id == R.id.ctg_Capsule) {
            if ( SelectCategories.getVisibility() == View.VISIBLE ){

                SelectCategories.setVisibility(View.GONE );
                (findViewById(R.id.ctg_Capsule)).setAlpha((float)0.25);

            }else{
                Group_Drops.setVisibility(View.GONE);
                SelectCategories.setVisibility(View.VISIBLE);
                (findViewById(R.id.ctg_Capsule)).setAlpha(1);
            }
            (findViewById(R.id.ctg_Tablet)).setAlpha((float) 0.25);
            (findViewById(R.id.ctg_Injection)).setAlpha((float)0.25);
            (findViewById(R.id.ctg_Drops)).setAlpha((float)0.25);
            addMedToDatabase("Capsule");

        }
        else if(id == R.id.ctg_Injection){
            if (Group_Drops.getVisibility() == View.GONE ){

                SelectCategories.setVisibility(View.GONE);

                Group_Drops.setVisibility(View.VISIBLE);
                (findViewById(R.id.ctg_Injection)).setAlpha(1);

            }else{

                Group_Drops.setVisibility(View.GONE);
                (findViewById(R.id.ctg_Injection)).setAlpha((float)0.25);

            }
            (findViewById(R.id.ctg_Tablet)).setAlpha((float) 0.25);
            (findViewById(R.id.ctg_Capsule)).setAlpha((float) 0.25);
            (findViewById(R.id.ctg_Drops)).setAlpha((float)0.25);
            addMedToDatabase("Injection");
        }
    }

    //Hide input keypad after focus changed
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}