package com.example.android.privatedairy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;


public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG=DbHelper.class.getSimpleName();

    public static final String DB_NAME="myapp.db";
    public static final int DB_VERSION=1;

    public static final String USER_TABLE="users";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_EMAIL="email";
    public static final String COLUMN_PASS="password";

    public static final String CREATE_USER_TABLE="CREATE TABLE "+USER_TABLE+"("+COLUMN_ID+
            " INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_EMAIL+" TEXT,"+COLUMN_PASS+" TEXT);";
    public DbHelper(Context context) {              //By creating a helper object it will create a database
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //It will use to create a database table.It is used when the database will first create
        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This method use when a database need to upgrade
        db.execSQL("DROP DATABASE IF EXISTS "+USER_TABLE);
        onCreate(db);

    }

    //Storing user detail in the database
    public void addUser(String email,String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL,email);
        values.put(COLUMN_PASS,password);

        long id = db.insert(USER_TABLE,null,values);
        db.close();

        Log.d(TAG," user inserted id "+id);
    }

    //For testing the user exists

    public boolean getUser(String email,String pass){
        String selectQuery="select * from "+USER_TABLE+" where "+COLUMN_EMAIL+" = "+"'"+email+"'"
                +" and "+COLUMN_PASS+" = "+"'"+pass+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
}
