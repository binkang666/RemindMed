package com.example.cecs491project.ui.medication;

import java.util.Comparator;

public class Medications {
    private String medicationName;
    private String categories;
    private int pillCount;
    private double dosage;
    private int refillCount;
    private String note;


    public Medications(String medicationName, String categories, int pillCount, double dosage, int refillCount, String Note) {
        this.medicationName = medicationName;
        this.categories = categories;
        this.pillCount = pillCount;
        this.dosage = dosage;
        this.refillCount = refillCount;
        this.note = Note;
    }


    public static Comparator<Medications> MedicationNameAZCompare = new Comparator<Medications>() {
        @Override
        public int compare(Medications m1, Medications m2) {
            return m1.getMedicationName().compareTo(m2.getMedicationName());
        }
    };

    public static Comparator<Medications> MedicationNameZACompare = new Comparator<Medications>() {
        @Override
        public int compare(Medications m1, Medications m2) {
            return m2.getMedicationName().compareTo(m1.getMedicationName());
        }
    };

    @Override
    public String toString() {
        return "Medications{" +
                ", medicationName='" + medicationName + '\'' +
                ", categories=" + categories +
                ", pillCount=" + pillCount +
                ", dosage=" + dosage +
                ", refillCount=" + refillCount +
                ", Note=" + note +
                '}';
    }

    public String getCategories(){
        return categories;
    }
    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public int getPillCount() {
        return pillCount;
    }

    public void setPillCount(int pillCount) {
        this.pillCount = pillCount;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public int getRefillCount() {
        return refillCount;
    }

    public void setRefillCount(int refillCount) {
        this.refillCount = refillCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
