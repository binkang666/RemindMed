package com.example.cecs491project.ui.reminder;

import android.text.format.Time;

import com.example.cecs491project.ui.medication.Medications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

enum Day{
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
}

public class Reminder {
    private String reminderName;
    private String medicationName;
    private ArrayList<Day> days; // if chosen everyday, then choose all days.
    private String time;
    private String startDate;
    private String endDate;
    private String note;

    // constructors
    public Reminder(){}

    public Reminder(String reminderName, String medicationName, ArrayList<Day> days, String time, String startDate, String endDate, String note) {
        this.reminderName = reminderName;
        this.medicationName = medicationName;
        this.days = days;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Reminder(String medicationName,
                    ArrayList<Day> days, String time, String startDate, String endDate, String note){
        this.medicationName = medicationName;
        this.days = days;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    public String toString(){
        return "Reminder{\n" +
                this.toString() + "\n" +
                "Days= " + this.days.toString() + "\n" +
                "Time= " + this.time + "\n"+
                "startDate= " + this.startDate + "\n" +
                "endDate= " + this.endDate +
                "}";

    }
    public static Comparator<Reminder> MedicationNameAZCompare = new Comparator<Reminder>() {
        @Override
        public int compare(Reminder m1, Reminder m2) {
            return m1.getMedicationName().compareTo(m2.getMedicationName());
        }
    };
/*
    public static Comparator<Medications> MedicationNameZACompare = new Comparator<Medications>() {
        @Override
        public int compare(Medications m1, Medications m2) {
            return m2.getMedicationName().compareTo(m1.getMedicationName());
        }
    };
*/
    //getters and setters

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
