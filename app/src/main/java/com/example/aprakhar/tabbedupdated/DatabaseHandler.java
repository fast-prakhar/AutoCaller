package com.example.aprakhar.tabbedupdated;

/**
 * Created by aprakhar on 22-Feb-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "temp";
    private static final String TABLE_CONTACTS = "contact";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_CHECKED = "active";
    private static final String ID = "id";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "( " + KEY_NUMBER + " TEXT, "
                + KEY_DATE + " TEXT, " + KEY_TIME + " TEXT, " + ID + " TEXT, " + KEY_CHECKED + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
        //Log.d("WWE","COMMAND Executed is " +CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void editNumber(CallerDetail contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateNumber = "UPDATE " + TABLE_CONTACTS + " SET " +
                KEY_NUMBER + " = " + contact.getNumber() + " WHERE " +
                ID + " = " + contact.getId();
        db.execSQL(updateNumber);
        db.close();


    }

    public void editEnabled(CallerDetail contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateEnabled = "UPDATE " + TABLE_CONTACTS + " SET " +
                KEY_CHECKED + " = " + contact.getEnabled() + " WHERE " +
                ID + " = " + contact.getId();
        db.execSQL(updateEnabled);
        db.close();

    }

    public boolean editDate(CallerDetail contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateDate = "UPDATE " + TABLE_CONTACTS + " SET " +
                KEY_DATE + " = " + contact.getDate() + " WHERE " +
                ID + " = " + contact.getId();
        db.execSQL(updateDate);
        db.close();
        return true;

    }

    public boolean editTime(CallerDetail contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateTime = "UPDATE " + TABLE_CONTACTS + " SET " +
                KEY_TIME + " = '" + contact.getTime() + "' WHERE " +
                ID + " = " + contact.getId();
        db.execSQL(updateTime);
        db.close();
        return true;
    }

    public void deleteEntry(CallerDetail contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();
        String updateDate = "DELETE FROM " + TABLE_CONTACTS + " WHERE " +
                ID + " = " + contact.getId();
        db.execSQL(updateDate);

    }

    public void addContact(CallerDetail contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NUMBER, contact.getNumber());
        contentValues.put(KEY_DATE, contact.getDate());
        contentValues.put(KEY_TIME, contact.getTime());
        contentValues.put(ID, contact.getId());
        contentValues.put(KEY_CHECKED, contact.getEnabled());
        db.insert(TABLE_CONTACTS, null, contentValues);
        db.close();
    }

    public int giveCount() {
        String query = "SELECT * FROM " + TABLE_CONTACTS;
        int i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            i++;
        }
        db.close();
        return i;
    }

    public String getID() {
        String ans = "";
        String query = "SELECT id FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            ans = ans + " " + String.valueOf(cursor.getInt(0));
        }
        db.close();
        return ans + "EOL";
    }

    public List<CallerDetail> getContactList() {

        List<CallerDetail> contactList = new ArrayList<CallerDetail>();
        String query = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            CallerDetail contact = new CallerDetail();
            contact.setNumber(cursor.getString(0));
            contact.setDate(cursor.getString(1));
            contact.setTime(cursor.getString(2));
            contact.setId(cursor.getString(3));
            contact.setEnabled(cursor.getString(4));
            contactList.add(contact);
        }
        cursor.close();
        db.close();
        return contactList;
    }
}
