package com.floorists.grannyspillbox.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.floorists.grannyspillbox.classes.History;
import com.floorists.grannyspillbox.classes.Medication;
import com.floorists.grannyspillbox.classes.ScheduledEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    // database version
    private static final int DATABASE_VERSION = 7;
    // database name
    private static final String DATABASE_NAME = "PillBoxDB";
    // table names
    private static final String TABLE_MEDICATION = "medication";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_SCHED_EVENT = "scheduled_event";
    // column names
    private static final String MED_ID = "id";
    private static final String MED_NAME = "name";
    private static final String MED_DESC = "description";
    private static final String MED_SERIAL = "serialNo";
    private static final String MED_QTY = "qty";
    private static final String MED_DATE = "date";
    private static final String MED_UOM = "uom";
    private static final String HIST_ID = "id";
    private static final String HIST_MED_ID = "medication_id";
    private static final String HIST_COMPLETED = "completed";
    private static final String HIST_QTY = "qty";
    private static final String HIST_DATE = "date";

    private static final String[] MED_COLUMNS = { MED_ID, MED_NAME, MED_DESC, MED_SERIAL, MED_QTY, MED_DATE, MED_UOM };
    private static final String[] HIST_COLUMNS = { HIST_ID, HIST_MED_ID, HIST_COMPLETED, HIST_QTY, HIST_DATE};
    private static final String[] SCHED_EVENT_COLUMNS = HIST_COLUMNS;
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private String getSimpleDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(todayDate);
    }

    private Date convertStringToDate(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date date = new Date();
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEDICATION_TABLE = "CREATE TABLE medication ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "description TEXT, "
                + "serialNo TEXT, "
                + "uom TEXT )";
        db.execSQL(CREATE_MEDICATION_TABLE);

        String CREATE_HISTORY_TABLE = "CREATE TABLE history ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "medication_id INTEGER, "
                + "completed INTEGER, "
                + "qty REAL, "
                + "date TEXT, "
                + "FOREIGN KEY(medication_id) REFERENCES medication(id))";
        db.execSQL(CREATE_HISTORY_TABLE);

        String CREATE_SCHEDULED_EVENT_TABLE = "CREATE TABLE scheduled_event ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "medication_id INTEGER, "
                + "completed INTEGER, "
                + "qty REAL, "
                + "date TEXT, "
                + "FOREIGN KEY(medication_id) REFERENCES medication(id))";
        db.execSQL(CREATE_SCHEDULED_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS medication");
        db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("DROP TABLE IF EXISTS scheduled_event");
        this.onCreate(db);
    }

    public void addMedication(Medication medication) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();
        values.put(MED_NAME, medication.getName());
        values.put(MED_DESC, medication.getDescription());
        values.put(MED_SERIAL, medication.getSerialNo());
        //values.put(MED_DATE, String.valueOf(medication.getDate()));
        values.put(MED_UOM, medication.getUom());
        //values.put(MED_QTY, medication.getQty());
        //insert
        db.insert(TABLE_MEDICATION, null, values);
        //close after transaction
        db.close();
    }

    public Medication getMedication(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEDICATION, MED_COLUMNS," id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        //if results != null, parse first
        if (cursor != null)
            cursor.moveToFirst();

        Medication medication = new Medication();
        medication.setId(Integer.parseInt(cursor.getString(0)));
        medication.setName(cursor.getString(1));
        medication.setDescription(cursor.getString(2));
        medication.setSerialNo(cursor.getString(3));
        //medication.setDate(convertStringToDate(cursor.getString(4)));
        medication.setUom(cursor.getString(5));
       // medication.setQty(cursor.getDouble(6));

        return medication;
    }

    public List<Medication> getAllMedications() {
        List<Medication> medList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICATION, null);

        //parse all results
        Medication medication;
        if (cursor.moveToFirst()) {
            do {
                medication = new Medication();
                medication.setId(Integer.parseInt(cursor.getString(0)));
                medication.setName(cursor.getString(1));
                medication.setDescription(cursor.getString(2));
                medication.setSerialNo(cursor.getString(3));

                //medication.setDate(convertStringToDate(cursor.getString(4)));
                medication.setUom(cursor.getString(5));
                /*
                try {
                    medication.setQty(cursor.getDouble(6));
                } catch (IllegalStateException ex) {
                    medication.setQty(0);
                } */

                medList.add(medication);
            } while (cursor.moveToNext());
        }
        return medList;
    }

    public void addToHistory(History record) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();

        values.put(HIST_MED_ID, record.getMedicationID());
        values.put(HIST_QTY, record.getQty());
        values.put(HIST_COMPLETED, record.isCompleted());
        values.put(HIST_DATE, String.valueOf(record.getTime()));

        //insert
        db.insert(TABLE_HISTORY, null, values);
        //close after transaction
        db.close();
    }

    public List<History> getHistoryForAllMeds() {
        List<History> histList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY, null);

        //parse all results
        History record;
        if (cursor.moveToFirst()) {
            do {
                record = new History();
                record.setId(Integer.parseInt(cursor.getString(0)));
                record.setMedicationID(Integer.parseInt(cursor.getString(1)));
                record.setCompleted(Integer.parseInt(cursor.getString(2)) != 0);
                record.setQty(cursor.getDouble(3));
                record.setTime(convertStringToDate(cursor.getString(4)));

                histList.add(record);
            } while (cursor.moveToNext());
        }
        return histList;
    }

    public List<History> getHistoryForToday() {
        List<History> histList = new ArrayList<>();
        String date = String.valueOf(Calendar.getInstance());
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " WHERE TRIM(serial) = '"+ serial.trim() + " AND TRIM(date) = '"+ date.trim() +  "'", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " WHERE TRIM(date) = '"+ date.trim() + "'", null);

        //parse all results
        History record;
        if (cursor.moveToFirst()) {
            do {
                record = new History();
                record.setId(Integer.parseInt(cursor.getString(0)));
                record.setMedicationID(Integer.parseInt(cursor.getString(1)));
                record.setCompleted(Integer.parseInt(cursor.getString(2)) != 0);
                record.setQty(cursor.getDouble(3));
                record.setTime(convertStringToDate(cursor.getString(4)));


                histList.add(record);
            } while (cursor.moveToNext());
        }
        return histList;
    }

    public void addScheduledEvent(ScheduledEvent record) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();

        values.put(HIST_MED_ID, record.getMedicationID());
        values.put(HIST_QTY, record.getQty());
        values.put(HIST_COMPLETED, record.isCompleted());
        values.put(HIST_DATE, String.valueOf(record.getTime()));

        //insert
        db.insert(TABLE_SCHED_EVENT, null, values);
        //close after transaction
        db.close();
    }

    public List<ScheduledEvent> getSchduledEvents() {
        List<ScheduledEvent> eventList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHED_EVENT, null);

        //parse all results
        ScheduledEvent record;
        if (cursor.moveToFirst()) {
            do {
                record = new ScheduledEvent();
                record.setId(Integer.parseInt(cursor.getString(0)));
                record.setMedicationID(Integer.parseInt(cursor.getString(1)));
                record.setCompleted(Integer.parseInt(cursor.getString(2)) != 0);
                record.setQty(cursor.getDouble(3));
                record.setTime(convertStringToDate(cursor.getString(4)));

                eventList.add(record);
            } while (cursor.moveToNext());
        }
        return eventList;
    }

    public List<ScheduledEvent> getSchedEventsForToday() {
        List<ScheduledEvent> eventList = new ArrayList<>();
        String date = String.valueOf(Calendar.getInstance());
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " WHERE TRIM(serial) = '"+ serial.trim() + " AND TRIM(date) = '"+ date.trim() +  "'", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHED_EVENT + " WHERE TRIM(date) = '"+ date.trim() + "'", null);

        //parse all results
        ScheduledEvent record;
        if (cursor.moveToFirst()) {
            do {
                record = new ScheduledEvent();
                record.setId(Integer.parseInt(cursor.getString(0)));
                record.setMedicationID(Integer.parseInt(cursor.getString(1)));
                record.setCompleted(Integer.parseInt(cursor.getString(2)) != 0);
                record.setQty(cursor.getDouble(3));
                record.setTime(convertStringToDate(cursor.getString(4)));


                eventList.add(record);
            } while (cursor.moveToNext());
        }
        return eventList;
    }

}

