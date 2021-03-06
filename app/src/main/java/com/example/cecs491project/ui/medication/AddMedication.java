package com.example.cecs491project.ui.medication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cecs491project.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddMedication extends AppCompatActivity {

    EditText et_medName, et_medPillCount, et_medDosage, et_medRefillCount, medicationNote;
    private Button SAVE;
    private View Card_View ;
    private Button cameraBtn;
    private ImageView medPic;


    private Button tablet, capsule, drops, injections;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Medication");
        initializePage();


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AddMedication.this,
                        Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddMedication.this,
                            new String[]{
                                    Manifest.permission.CAMERA
                            }, 100);
                }
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 100);
            }
        });

    }


    private void initializePage() {
        et_medName = findViewById(R.id.editTextMedicationName);
        et_medPillCount = findViewById(R.id.editTextPillCount);
        et_medDosage = findViewById(R.id.editTextDosage);
        et_medRefillCount = findViewById(R.id.editTextRefillCount);
        SAVE = findViewById(R.id.SAVE);
        Card_View = findViewById(R.id.Card_View);
        medicationNote = findViewById(R.id.medication_note);
        cameraBtn = findViewById(R.id.camera_to_add_med);
        medPic = findViewById(R.id.med_pics);

        tablet = findViewById(R.id.ctg_Tablet);
        capsule = findViewById(R.id.ctg_Capsule);
        drops = findViewById(R.id.ctg_Drops);
        injections = findViewById(R.id.ctg_Injection);

        SAVE.setZ(20);
        Card_View.setZ(2);
    }

    private void makeSnackBarMessage(String message){
        Snackbar.make(Card_View, message, Snackbar.LENGTH_SHORT).show();
    }

    private void addMedToDatabase(String categories){

        SAVE.setOnClickListener(view -> {

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String, Object> tabletMap = new HashMap<>();

            String medName = et_medName.getText().toString();
            tabletMap.put("Categories", categories);
            int pillCount = 0;
            double dosage = 0.0;
            int refillCount = 0;
            String Note = "";
            try {
                pillCount = Integer.parseInt(et_medPillCount.getText().toString());
                tabletMap.put("Pill Count", pillCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                dosage = Double.parseDouble(et_medDosage.getText().toString());
                tabletMap.put("Dosage", dosage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                refillCount = Integer.parseInt(et_medRefillCount.getText().toString());
                tabletMap.put("Refill Count", refillCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                Note = medicationNote.getText().toString();
                tabletMap.put("Medication Note", Note);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DocumentReference userTablet = null;
            if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
                userTablet = db.collection("Anonymous User Database")
                        .document(userID).collection("Medication").document(medName);
            }
            else{
                userTablet = db.collection("User Database")
                        .document(userID).collection("Medication").document(medName);
            }
            try {

                Medications med = new Medications(medName, categories, pillCount, dosage, refillCount, Note);

                userTablet.set(med).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            makeSnackBarMessage("Medication added");
                        } else {
                            makeSnackBarMessage("Failed");
                        }
                    }
                });
                finish();
            }catch (Exception e){
                makeSnackBarMessage("Medication name and type must be entered.");
            }
        });
    }

    // If the tablet, capsule... etc buttons are clicked on this function will instantiate
    //
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap captureImage= (Bitmap) data.getExtras().get("data");
            medPic.setImageBitmap(captureImage);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        return true;
    }
}