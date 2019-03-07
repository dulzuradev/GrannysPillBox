package com.floorists.grannyspillbox.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.floorists.grannyspillbox.classes.History;
import com.floorists.grannyspillbox.classes.Medication;
import com.floorists.grannyspillbox.classes.ScheduledEvent;
import com.floorists.grannyspillbox.classes.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    // database version
    private static final int DATABASE_VERSION = 10;
    // database name
    private static final String DATABASE_NAME = "PillBoxDB";
    // table names
    private static final String TABLE_USER = "user";
    private static final String TABLE_MEDICATIONS = "medications";
    private static final String TABLE_EVENTS = "events";
    // column names
    private static final String MED_ID = "id";
    private static final String MED_NAME = "name";
    private static final String MED_DESC = "description";
    private static final String MED_SERIAL = "serialNo";
    private static final String MED_QTY = "qty";
    private static final String MED_UOM = "uom";

    private static final String EVENT_ID = "id";
    private static final String EVENT_MED_ID = "med_id";
    private static final String EVENT_COMPLETED = "completed";
    private static final String EVENT_QTY = "qty";
    private static final String EVENT_DATE = "date";
    private static final String _ID = "id";
    private static final String USER_FIRST = "first_name";
    private static final String USER_LAST = "last_name";
    private static final String USER_PHONE = "email";
    private static final String USER_EMAIL = "phone";
    private static final String USER_EMERGENCY_CONTACT_ID = "emergency_contact_id";
    private static final String[] MEDS_COLUMNS = { MED_ID, MED_NAME, MED_DESC, MED_SERIAL, MED_QTY, MED_UOM };
    private static final String[] EVENTS_COLUMNS = { EVENT_ID, EVENT_MED_ID, EVENT_COMPLETED, EVENT_QTY, EVENT_DATE};
    private static final String[] USER_COLUMNS = {_ID, USER_FIRST, USER_LAST, USER_PHONE, USER_EMAIL, USER_EMERGENCY_CONTACT_ID};
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        String CREATE_MEDICATIONS_TABLE = "CREATE TABLE IF NOT EXISTS medications ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "description TEXT, "
                + "serialNo TEXT, "
                + "qty REAL, "
                + "uom TEXT )";
        db.execSQL(CREATE_MEDICATIONS_TABLE);

        String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS events ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "med_id INTEGER, "
                + "completed INTEGER, "
                + "qty REAL, "
                + "date TEXT, "
                + "FOREIGN KEY(med_id) REFERENCES medication(id))";
        db.execSQL(CREATE_EVENTS_TABLE);
        String CREATE_USER_TABLE = "CREATE TABLE USER ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "first_name TEXT, "
                + "last_name TEXT, "
                + "phone TEXT, "
                + "email TEXT, "
                + "emergency_contact_id INTEGER,"
                + "FOREIGN KEY(emergency_contact_id) REFERENCES user(id))";
        db.execSQL(CREATE_USER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS medication");
        db.execSQL("DROP TABLE IF EXISTS scheduled_event");
        db.execSQL("DROP TABLE IF EXISTS user");
        this.onCreate(db);
    }

    public int addMedication(Medication medication) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();
        values.put(MED_NAME, medication.getName());
        values.put(MED_DESC, medication.getDescription());
        values.put(MED_SERIAL, medication.getSerialNo());
        values.put(MED_QTY, medication.getQty());
        values.put(MED_UOM, medication.getUom());
        //insert
        db.insert(TABLE_MEDICATIONS, null, values);
        //close after transaction
        //db.close();
        return (int) getMedicationByName(medication.name).id;
    }

    public Medication getMedicationByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEDICATIONS, MEDS_COLUMNS," name = ?", new String[] { String.valueOf(name) }, null, null, null, null);
        //if results != null, parse first
        if (cursor != null)
            cursor.moveToFirst();

        Medication medication = new Medication();
        medication.setId(Integer.parseInt(cursor.getString(0)));
        medication.setName(cursor.getString(1));
        medication.setDescription(cursor.getString(2));
        medication.setSerialNo(cursor.getString(3));
        medication.setQty(cursor.getDouble(4));
        medication.setUom(cursor.getString(5));
        return medication;
    }

    public Medication getMedication(long id) {
        if (id == 0)
            return null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEDICATIONS, MEDS_COLUMNS," id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        //if results != null, parse first
        if (cursor != null)
            cursor.moveToFirst();

        Medication medication = new Medication();
        medication.setId(Integer.parseInt(cursor.getString(0)));
        medication.setName(cursor.getString(1));
        medication.setDescription(cursor.getString(2));
        medication.setSerialNo(cursor.getString(3));
        medication.setQty(cursor.getDouble(4));
        medication.setUom(cursor.getString(5));
        return medication;
    }

    public List<Medication> getAllMedications() {
        List<Medication> medList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICATIONS, null);

        //parse all results
        Medication medication;
        if (cursor.moveToFirst()) {
            do {
                medication = new Medication();
                medication.setId(Integer.parseInt(cursor.getString(0)));
                medication.setName(cursor.getString(1));
                medication.setDescription(cursor.getString(2));
                medication.setSerialNo(cursor.getString(3));
                try {
                    medication.setQty(cursor.getDouble(4));
                } catch (IllegalStateException ex) {
                    medication.setQty(0);
                }
                medication.setUom(cursor.getString(5));

                medList.add(medication);
            } while (cursor.moveToNext());
        }
        return medList;
    }
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();
        if (user.getId() > 0) {
            values.put(_ID, user.getId());
        }
        values.put(USER_FIRST, user.getFirstName());
        values.put(USER_LAST, user.getLastName());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_EMAIL, user.getEmail());
        if (user.getEmergencyContact() != null && user.getEmergencyContact().getId() > 0) {
            values.put(USER_EMERGENCY_CONTACT_ID, user.getEmergencyContact().getId());
            addEmergencyUser(user.getEmergencyContact());
        }


        if(user.getId() > 0 && getUser(user.getId()) != null ) {
            db.update(TABLE_USER,values, "id="+String.valueOf(user.getId()), null);
        } else {
            db.insert(TABLE_USER, null, values);
        }

        //insert

        //close after transaction
        db.close();
    }
    public void addEmergencyUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();
        if (user.getId() > 0) {
            values.put(_ID, user.getId());
        }
        values.put(USER_FIRST, user.getFirstName());
        values.put(USER_LAST, user.getLastName());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_EMAIL, user.getEmail());

        if(user.getId() > 0 && getUser(user.getId()) != null ) {
            db.update(TABLE_USER,values, "id="+String.valueOf(user.getId()), null);
        } else {
            db.insert(TABLE_USER, null, values);
        }

        //insert

        //close after transaction
    }

    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, USER_COLUMNS," id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        //if results != null, parse first
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();
        if(cursor.getCount() <= 0)
        {
            return null;
        }
        user.setId(Integer.parseInt(cursor.getString(0)));
        user.setFirstName(cursor.getString(1));
        user.setLastName(cursor.getString(2));
        user.setPhone(cursor.getString(3));
        //medication.setDate(convertStringToDate(cursor.getString(4)));
        user.setEmail(cursor.getString(5));

        try {
            int emergencyContactId = Integer.parseInt(cursor.getString(6));
            if(emergencyContactId > 0) {
                user.setEmergencyContact(getUser(emergencyContactId));
            }
        } catch (Exception e){

        }


        // medication.setQty(cursor.getDouble(6));

        return user;
    }

    public void addScheduledEvent(ScheduledEvent record) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();

        values.put(EVENT_MED_ID, record.getMedicationID());
        values.put(EVENT_COMPLETED, record.isCompleted());
        values.put(EVENT_QTY, record.getQty());
        values.put(EVENT_DATE, String.valueOf(record.getTime()));

        //insert
        db.insert(TABLE_EVENTS, null, values);
        //close after transaction
        //db.close();
    }

    public List<ScheduledEvent> getSchduledEvents() {
        List<ScheduledEvent> eventList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);

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
                record.setMedication(getMedication(Integer.parseInt(cursor.getString(1))));
                eventList.add(record);
            } while (cursor.moveToNext());
        }
        return eventList;
    }

    public List<ScheduledEvent> getSchedEventsForToday() {
        List<ScheduledEvent> eventList = new ArrayList<>();
        String date = String.valueOf(Calendar.getInstance());
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS + " WHERE TRIM(date) = '"+ date.trim() + "'", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);

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
                record.setMedication(getMedication(Integer.parseInt(cursor.getString(1))));

                eventList.add(record);
            } while (cursor.moveToNext());
        }
        return eventList;
    }

}

