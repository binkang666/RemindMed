package com.example.cecs491project.ui.medication;

import java.util.Comparator;

public class Medications {
    private int medicineNumber;
    private String medicationName;
    private int pillCount;
    private double dosage;
    private int refillCount;


    public Medications(int medicineNumber, String medicationName, int pillCount, double dosage, int refillCount) {
        this.medicineNumber = medicineNumber;
        this.medicationName = medicationName;
        this.pillCount = pillCount;
        this.dosage = dosage;
        this.refillCount = refillCount;
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

    public static Comparator<Medications> MedicationNameDateCreatedCompare = new Comparator<Medications>() {
        @Override
        public int compare(Medications m1, Medications m2) {
            return m1.getMedicineNumber() - m2.getMedicineNumber();
        }
    };

    @Override
    public String toString() {
        return "Medications{" +
                "medicineNumber=" + medicineNumber +
                ", medicationName='" + medicationName + '\'' +
                ", pillCount=" + pillCount +
                ", dosage=" + dosage +
                ", refillCount=" + refillCount +
                '}';
    }

    public int getMedicineNumber() {
        return medicineNumber;
    }

    public void setMedicineNumber(int medicineNumber) {
        this.medicineNumber = medicineNumber;
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


}
