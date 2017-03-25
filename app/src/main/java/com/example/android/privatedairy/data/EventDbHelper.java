package com.example.android.privatedairy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class EventDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "personalEvents.db";


    private static final int DATABASE_VERSION = 1;
    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_EVENTS_TABLE =  "CREATE TABLE " + EventContract.EventEntry.TABLE_NAME + " ("
                + EventContract.EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventContract.EventEntry.COLUMN_EVENT + " TEXT NOT NULL, "+EventContract.EventEntry.COLUMN_DATE +
                " TEXT NOT NULL, "+ EventContract.EventEntry.COLUMN_NOTE+" TEXT NOT NULL, "
                + EventContract.EventEntry.COLUMN_TIME + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_EVENTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
