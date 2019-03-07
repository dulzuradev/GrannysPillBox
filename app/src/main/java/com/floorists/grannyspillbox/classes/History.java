package com.floorists.grannyspillbox.classes;

import com.floorists.grannyspillbox.Medication;

import java.util.Date;

public class History {
    public int id;
    public double qty;
    public Date time;
    public int medicationID;
    public Medication medication;
    public boolean completed;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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
