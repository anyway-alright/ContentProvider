package com.example.android.privatedairy;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.app.LoaderManager;

import android.content.CursorLoader;

import android.content.Loader;
import android.database.Cursor;


import com.example.android.privatedairy.data.EventContract;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private EditText eventEdit;
    private EditText noteEdit;
    private EditText timeEdit;
    private EditText dateEdit;
    private Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentUri = intent.getData();
        if (currentUri == null) {
            setTitle("Add Event");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Event");
            getLoaderManager().initLoader(1, null, this);
        }


        eventEdit = (EditText) findViewById(R.id.event_desc);

        noteEdit=(EditText) findViewById(R.id.event_note);

        timeEdit = (EditText) findViewById(R.id.time_desc);
        dateEdit = (EditText) findViewById(R.id.dateDesc);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                // Save pet to database
                savePet();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:

                showDeleteConfirmationDialog();
                return true;
        }
            return super.onOptionsItemSelected(item);
        }


    private void savePet() {
        String eventString = eventEdit.getText().toString().trim();
        String noteString = noteEdit.getText().toString().trim();
        String timeString = timeEdit.getText().toString().trim();
        String dateString = dateEdit.getText().toString().trim();


        if (currentUri == null &&
                TextUtils.isEmpty(eventString) && TextUtils.isEmpty(timeString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_EVENT, eventString);
        values.put(EventContract.EventEntry.COLUMN_TIME, timeString);
        values.put(EventContract.EventEntry.COLUMN_NOTE,noteString);
        values.put(EventContract.EventEntry.COLUMN_DATE,dateString);

        if (currentUri == null) {
            Uri newUri = getContentResolver().insert(EventContract.EventEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "Faied",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Success",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(currentUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_event_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_event_successful),
                        Toast.LENGTH_SHORT).show();
            }


        }
    }
    private void deleteEvent() {

        if (currentUri != null) {

            int rowsDeleted = getContentResolver().delete(currentUri, null, null);
            finish();
        }
    }
    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteEvent();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_EVENT,
                EventContract.EventEntry.COLUMN_NOTE,
                EventContract.EventEntry.COLUMN_TIME,
                EventContract.EventEntry.COLUMN_DATE
                };
        return new CursorLoader(this, currentUri, projection, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT);


            int noteColumnIndex=cursor.getColumnIndex(EventContract.EventEntry.COLUMN_NOTE);

            int timeColumnIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TIME);

            int dateColumnIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DATE);



            String currentName = cursor.getString(nameColumnIndex);

           String currentNote=cursor.getString(noteColumnIndex);

            String currentTime = cursor.getString(timeColumnIndex);
            String currentDate = cursor.getString(dateColumnIndex);

            eventEdit.setText(currentName);
            noteEdit.setText(currentNote);
            timeEdit.setText(currentTime);
            dateEdit.setText(currentDate);


        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        eventEdit.setText("");
       noteEdit.setText("");
        timeEdit.setText("");
        dateEdit.setText("");

    }
}
