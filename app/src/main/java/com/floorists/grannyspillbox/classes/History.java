package com.floorists.grannyspillbox.classes;

public class History {
    public double qty;
    public String time;
    public int medicationID;
    public Medication medication;
    public boolean completed;


    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(int medicationID) {
        this.medicationID = medicationID;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
