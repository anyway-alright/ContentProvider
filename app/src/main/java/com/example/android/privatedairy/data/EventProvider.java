package com.example.android.privatedairy.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Lenovo on 2/8/2017.
 */

public class EventProvider extends ContentProvider {
   private EventDbHelper mDBHelper;

    private static final int EVENTS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int EVENT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENTS, EVENTS);


        sUriMatcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENTS + "/#", EVENT_ID);
    }
    @Override
    public boolean onCreate() {
        mDBHelper=new EventDbHelper(getContext());
        return true;
    }

    @Nullable

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDBHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:

                cursor = database.query(EventContract.EventEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EVENT_ID:

                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(EventContract.EventEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
       return insertEvent(uri,values);
    }

    private Uri insertEvent(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDBHelper.getWritableDatabase();


        long id = database.insert(EventContract.EventEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);

    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:
                // Delete all rows that match the selection and selection args
                getContext().getContentResolver().notifyChange(uri,null);
                return database.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
            case EVENT_ID:
                // Delete a single row given by the ID in the URI
                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                getContext().getContentResolver().notifyChange(uri,null);
                return database.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:
                return updateEvent(uri, contentValues, selection, selectionArgs);
            case EVENT_ID:

                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateEvent(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateEvent(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database =mDBHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        getContext().getContentResolver().notifyChange(uri,null);
        return database.update(EventContract.EventEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
