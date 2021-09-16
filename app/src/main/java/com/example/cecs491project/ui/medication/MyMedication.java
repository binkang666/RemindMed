package com.example.cecs491project.ui.medication;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyMedication extends Application {
    private static List<Medications> medicationsList = new ArrayList<Medications>();
    private static int nextMedNumber;

    public MyMedication() {
        //fillMedicationsList();
    }


    private void fillMedicationsList() {
       // Medications m0 = new Medications(0, "Ibuprofen", 20, 800, 3);
        //Medications m1 = new Medications(1, "Etodolac", 30, 800, 0);

//        medicationsList.addAll(Arrays.asList(new Medications[] {m0, m1}));
    }

    public static List<Medications> getMedicationsList() {
        return medicationsList;
    }

    public static void setMedicationsList(List<Medications> medicationsList) {
        MyMedication.medicationsList = medicationsList;
    }

    public static int getNextMedNumber() {
        return nextMedNumber;
    }

    public static void setNextMedNumber(int nextMedNumber) {
        MyMedication.nextMedNumber = nextMedNumber;
    }
}
