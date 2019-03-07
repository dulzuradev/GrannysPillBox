package com.floorists.grannyspillbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    // column names
    private static final String MED_ID = "id";
    private static final String MED_NAME = "name";
    private static final String MED_DESC = "description";
    private static final String MED_SERIAL = "serialNo";
    private static final String MED_QTY = "qty";
    private static final String MED_DATE = "date";
    private static final String MED_UOM = "uom";

    private static final String[] MED_COLUMNS = { MED_ID, MED_NAME, MED_DESC, MED_SERIAL, MED_QTY, MED_DATE, MED_UOM };
    private static final String[] HIST_COLUMNS = { MED_ID, MED_NAME, MED_DESC, MED_SERIAL, MED_QTY, MED_DATE, MED_UOM };

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
            // TODO Auto-generated catch block
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
                + "date TEXT, "
                + "uom TEXT, "
                + "qty REAL )";
        db.execSQL(CREATE_MEDICATION_TABLE);

        String CREATE_HISTORY_TABLE = "CREATE TABLE history ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "description TEXT, "
                + "serialNo TEXT, "
                + "date TEXT, "
                + "uom TEXT, "
                + "qty REAL )";
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS medication");
        db.execSQL("DROP TABLE IF EXISTS history");
        this.onCreate(db);
    }

    public void AddMedication(Medication medication) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();
        values.put(MED_NAME, medication.getName());
        values.put(MED_DESC, medication.getDescription());
        values.put(MED_SERIAL, medication.getSerialNo());
        values.put(MED_DATE, String.valueOf(medication.getDate()));
        values.put(MED_UOM, medication.getUom());
        values.put(MED_QTY, medication.getQty());
        //insert
        db.insert(TABLE_MEDICATION, null, values);
        //close after transaction
        db.close();
    }

    public Medication GetMedication(long id) {
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
        medication.setDate(convertStringToDate(cursor.getString(4)));
        medication.setUom(cursor.getString(5));
        medication.setQty(cursor.getDouble(6));

        return medication;
    }

    public List<Medication> GetAllMedications() {
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
                medication.setDate(convertStringToDate(cursor.getString(4)));
                medication.setUom(cursor.getString(5));
                try {
                    medication.setQty(cursor.getDouble(6));
                } catch (IllegalStateException ex) {
                    medication.setQty(0);
                }

                medList.add(medication);
            } while (cursor.moveToNext());
        }
        return medList;
    }

    public void AddToHistory(History record) {
        SQLiteDatabase db = this.getWritableDatabase();
        //make values
        ContentValues values = new ContentValues();
        values.put(MED_NAME, record.getName());
        values.put(MED_DESC, record.getDescription());
        values.put(MED_SERIAL, record.getSerialNo());
        values.put(MED_DATE, String.valueOf(record.getDate()));
        values.put(MED_UOM, record.getUom());
        values.put(MED_QTY, record.getQty());
        //insert
        db.insert(TABLE_HISTORY, null, values);
        //close after transaction
        db.close();
    }

    public List<History> GetHistoryForAllMeds() {
        List<History> histList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY, null);

        //parse all results
        History record;
        if (cursor.moveToFirst()) {
            do {
                record = new History();
                record.setId(Integer.parseInt(cursor.getString(0)));
                record.setName(cursor.getString(1));
                record.setDescription(cursor.getString(2));
                record.setSerialNo(cursor.getString(3));
                record.setDate(convertStringToDate(cursor.getString(4)));
                record.setUom(cursor.getString(5));
                try {
                    record.setQty(cursor.getDouble(6));
                } catch (IllegalStateException ex) {
                    record.setQty(0);
                }

                histList.add(record);
            } while (cursor.moveToNext());
        }
        return histList;
    }
}

