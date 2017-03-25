package com.example.android.privatedairy;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.privatedairy.data.EventContract;
import com.example.android.privatedairy.data.EventDbHelper;

public class EventsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int  EVENT_LOADER=0;
    EventCursorAdapter  mCursorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);



       FloatingActionButton button= (FloatingActionButton) findViewById(R.id.insert_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EventsActivity.this,EditorActivity.class);
                startActivity(intent);

            }
        });

        ListView listView=(ListView) findViewById(R.id.list_events);
        mCursorAdapter =new EventCursorAdapter(this,null);
        listView.setAdapter(mCursorAdapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(EventsActivity.this,EditorActivity.class);
                Uri currentEventUri= ContentUris.withAppendedId(EventContract.EventEntry.CONTENT_URI,id);
                intent.setData(currentEventUri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(EVENT_LOADER,null,this);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={  EventContract.EventEntry._ID,
              EventContract.EventEntry.COLUMN_EVENT,
                EventContract.EventEntry.COLUMN_DATE,
        };
        return new CursorLoader(this, EventContract.EventEntry.CONTENT_URI,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:

               showDeleteConfirmationDialog();
                return true;




        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(EventContract.EventEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete?");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
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





}
