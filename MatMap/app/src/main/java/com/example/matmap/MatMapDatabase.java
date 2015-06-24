package com.example.matmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database for whole application
 */
public class MatMapDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MatMapDatabase";

    public MatMapDatabase(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS rooms");
        db.execSQL("CREATE TABLE rooms (_id INTEGER PRIMARY KEY AUTOINCREMENT, room_name TEXT, " +
                   "top_neighbor TEXT, bottom_neighbor TEXT, left_neighbor TEXT, " +
                   "right_neighbor TEXT);");

        //db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("CREATE TABLE history (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "room_name TEXT, timestamp REAL);");

        //db.execSQL("DROP TABLE IF EXISTS search_data");
        db.execSQL("CREATE TABLE search_data (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "group_id INTEGER, timestamp REAL, room_name TEXT, " +
                   "BSSID TEXT, strength REAL, device TEXT, SSID TEXT);");

        //db.execSQL("DROP TABLE IF EXISTS record_group")
        db.execSQL("CREATE TABLE record_group (record_group_id INTEGER);");

        //db.execSQL("DROP TABLE IF EXISTS neighbours")
        db.execSQL("CREATE TABLE neighbors (" +
                "room_name, " +
                "neighbor, " +
                "PRIMARY KEY (room_name, neighbor) " +
        ");");

        /*
         * Puts single record into table record_group_id
         * Should be deleted and changed in future
         */
        ContentValues cv = new ContentValues();
        cv.put("record_group_id", 1);
        db.insert("record_group", "record_group_id", cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
