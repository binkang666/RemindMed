package com.example.cecs491project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddOrEditMedication extends AppCompatActivity {

    List<Medications> medicationsList;
    MyMedication myMedication = (MyMedication) this.getApplication();
    EditText et_medNumber, et_medName, et_medPillCount, et_medDosage, et_medRefillCount;
    TextView tv_medNumber;
    int medNumber;
    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_medication);

        btn_ok = findViewById(R.id.buttonConfirm);
        medicationsList = myMedication.getMedicationsList();
        et_medName = findViewById(R.id.editTextMedicationName);
        et_medPillCount = findViewById(R.id.editTextPillCount);
        et_medDosage = findViewById(R.id.editTextDosage);
        et_medRefillCount = findViewById(R.id.editTextRefillCount);
        tv_medNumber = findViewById(R.id.textViewMedID);

        Intent i = getIntent();
        medNumber = i.getIntExtra("id", -1);
        Medications medications = null;

        if (medNumber >= 0)
        {
            //edit medicine
            for(Medications m : medicationsList)
            {
                if (m.getMedicineNumber() == medNumber)
                {
                    medications = m;
                }
            }
            et_medName.setText(medications.getMedicationName());
            et_medPillCount.setText(String.valueOf(medications.getPillCount()));
            et_medDosage.setText(String.valueOf(medications.getDosage()));
            et_medRefillCount.setText(String.valueOf(medications.getRefillCount()));
            tv_medNumber.setText(String.valueOf(medNumber));
        }
        else
        {
            //create new medicine
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medNumber >= 0)
                {
                    //update
                    Medications updatedMedication = new Medications(medNumber,et_medName.getText().toString(), Integer.parseInt(et_medPillCount.getText().toString()), Double.parseDouble(et_medDosage.getText().toString()), Integer.parseInt(et_medRefillCount.getText().toString()));
                    medicationsList.set(medNumber, updatedMedication);
                    Toast.makeText(getApplicationContext(), "Medicine Edited", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    //add new medicine
                    //create Medication object
                    int nextMedNumber = MyMedication.getNextMedNumber();
                    Medications newMedication = new Medications(nextMedNumber, et_medName.getText().toString(), Integer.parseInt(et_medPillCount.getText().toString()), Double.parseDouble(et_medDosage.getText().toString()), Integer.parseInt(et_medRefillCount.getText().toString()));
                    //add the object to the global list of medications
                    medicationsList.add(newMedication);
                    MyMedication.setNextMedNumber(++nextMedNumber);
                }


                //go back to main activity
                Intent i1 = new Intent(AddOrEditMedication.this, MedicationsActivity.class);
                startActivity(i1);
            }


        });
    }

    /*
    public void ConfirmButton(View v)
    {
        if (medNumber >= 0)
        {
            //update
            Medications updatedMedication = new Medications(medNumber,et_medName.getText().toString(), Integer.parseInt(et_medPillCount.getText().toString()), Integer.parseInt(et_medDosage.getText().toString()), Integer.parseInt(et_medRefillCount.getText().toString()));
            medicationsList.set(medNumber, updatedMedication);
        }
        else
        {
            //add new medicine
            //create Medication object
            int nextMedNumber = MyMedication.getNextMedNumber();
            Medications newMedication = new Medications(nextMedNumber, et_medName.getText().toString(), Integer.parseInt(et_medPillCount.getText().toString()), Integer.parseInt(et_medDosage.getText().toString()), Integer.parseInt(et_medRefillCount.getText().toString()));
            //add the object to the global list of medications
            medicationsList.add(newMedication);
            MyMedication.setNextMedNumber(nextMedNumber++);
        }


        //go back to main activity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }*/

    public void CancelButton(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        startActivity(i);
    }

}