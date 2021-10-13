package com.example.cecs491project.ui.reminder;

import android.text.format.Time;

import com.example.cecs491project.ui.medication.Medications;

import java.util.ArrayList;
import java.util.Arrays;

enum Day{
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
}

public class Reminder extends Medications{
    private ArrayList<Day> days; // if chosen everyday, then choose all days.
    private String time;
    private String startDate;
    private String endDate;
    private String note;

    // constructors
    public Reminder(){}

    public Reminder(String medName, String categories, int pillCount, double dosage, int refillCount, String medNote
            , ArrayList<Day> days, String time, String startDate, String endDate, String note){
        super(medName,categories,pillCount,dosage,refillCount,medNote);
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
