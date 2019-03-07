package com.floorists.grannyspillbox.classes;

import java.util.ArrayList;
import java.util.Date;

public class ScheduledEvent {

    public int id;
    public double qty;
    public Date time;
    public int medicationID;
    public Medication medication;
    public boolean completed;

    public ScheduledEvent(){

    }
    public ScheduledEvent(int id, double qty, Date time, int medicationID, Medication medication, boolean completed) {
        this.id = id;
        this.qty = qty;
        this.time = time;
        this.medicationID = medicationID;
        this.medication = medication;
        this.completed = completed;
    }

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

    @SuppressWarnings("deprecation")
    public static ArrayList<ScheduledEvent> getMockData() {
        ArrayList<ScheduledEvent> mockData = new ArrayList<>();

//        mockData.add(new ScheduledEvent(1,  2.0, new Date(2019, 1, 1, 0, 0, 0), 1, null, false));
        mockData.add(new ScheduledEvent(2,  2, new Date(2019, 2, 1, 0, 0, 0), 1, null, true));

        return mockData;
    }
}
