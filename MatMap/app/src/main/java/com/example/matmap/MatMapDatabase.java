package com.example.matmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by richard on 5.2.2015.
 */
public class MatMapDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MatMapDatabase";

    /*
    V buducnosti sa bude vyuzivat pri prehladavani
     */
    private static final String ROOM_NAME = "room_name";
    private static final String TOP_NEIGHBOR = "top_neighbor";
    private static final String BOTTOM_NEIGHBOR = "bottom_neighbor";
    private static final String LEFT_NEIGHBOR = "left_neighbor";
    private static final String RIGHT_NEIGHBOR = "right_neighbor";


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

        //db.execSQL("DROP TABLE IF EXISTS ")
        db.execSQL("CREATE TABLE record_group (record_group_id INTEGER);");

        ContentValues cv = new ContentValues();

        cv.put(ROOM_NAME, "M I");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M II");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M III");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M IV");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M V");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M VI");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M VII");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "A");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "B");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "C");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "F1");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "F2");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M 217");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "M 218");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        cv.put(ROOM_NAME, "F1 109");
        cv.put(TOP_NEIGHBOR, "nothing_for_now");
        cv.put(BOTTOM_NEIGHBOR, "nothing_for_now");
        cv.put(RIGHT_NEIGHBOR, "nothing_for_now");
        cv.put(LEFT_NEIGHBOR, "nothing_for_now");
        db.insert("rooms", ROOM_NAME, cv);

        /*
         * Vlozi jediny riadok do tabulky group_id
         */
        cv.clear();

        cv.put("record_group_id", 1);
        db.insert("record_group", "record_group_id", cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
