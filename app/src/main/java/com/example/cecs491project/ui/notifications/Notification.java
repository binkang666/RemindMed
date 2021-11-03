package com.example.cecs491project.ui.notifications;

public class Notification {
    private String medicationName;
    private String reminderName;
    private String time;
    private int takenOrNot;

    public Notification(){ }

    public Notification(String reminderName, String medicationName, String time, int takenOrNot) {
        this.medicationName = medicationName;
        this.reminderName = reminderName;
        this.time = time;
        this.takenOrNot = takenOrNot;
    }

    public int getTakenOrNot() {
        return takenOrNot;
    }

    public void setTakenOrNot(int takenOrNot) {
        this.takenOrNot = takenOrNot;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
