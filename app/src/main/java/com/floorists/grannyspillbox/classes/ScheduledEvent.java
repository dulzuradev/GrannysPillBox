package com.floorists.grannyspillbox.classes;

import java.util.Date;

public class ScheduledEvent {

    public int id;
    public double qty;
    public Date time;
    public int medicationID;
    public Medication medication;
    public boolean completed;

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(int medicationID) {
        this.medicationID = medicationID;
    }



    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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

    public Medication getMedication() {
        if(medication == null) {
            medication = new Medication();
            medication.id = medicationID;
        }
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }
}
