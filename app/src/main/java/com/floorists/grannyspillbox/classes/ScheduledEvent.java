package com.floorists.grannyspillbox.classes;

import com.floorists.grannyspillbox.Medication;

public class ScheduledEvent {

    public double qty;
    public String time;
    public int medicationID;
    public Medication medication;
    public boolean completed;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
