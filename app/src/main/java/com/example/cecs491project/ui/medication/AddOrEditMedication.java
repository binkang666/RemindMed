package com.example.cecs491project.ui.medication;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cecs491project.R;

import java.util.List;
import java.util.Objects;

public class AddOrEditMedication extends AppCompatActivity {

    List<Medications> medicationsList;
    MyMedication myMedication = (MyMedication) this.getApplication();
    EditText et_medName, et_medPillCount, et_medDosage, et_medRefillCount;
    private TextView SAVE;
    private View Card_View ;
    private ImageView ASClose ;

    private Button tablet, capsule, drops, injections;

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
        ASClose = findViewById(R.id.ASClose);
        SAVE = findViewById(R.id.SAVE);
        Card_View = findViewById(R.id.Card_View);

        tablet = findViewById(R.id.ctg_Tablet);
        capsule = findViewById(R.id.ctg_Capsule);
        drops = findViewById(R.id.ctg_Drops);
        injections = findViewById(R.id.ctg_Injection);

        SAVE.setZ(20);
        Card_View.setZ(2);
        ASClose.setZ(20);
    }

    public void onClickCategories( View view ){
        int id = view.getId();
        TransitionManager.beginDelayedTransition((ConstraintLayout)findViewById(R.id.Card_View));
        View SelectCategories = findViewById(R.id.select_categories);
        View Group_Drops = findViewById(R.id.Drop_Group);

        if(id == R.id.ctg_Tablet) {
            //TODO:change the color of the button when pressed and then changed back after different option is selected.
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
        }
    }
}