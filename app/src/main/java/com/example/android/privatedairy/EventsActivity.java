package com.example.android.privatedairy;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.privatedairy.data.EventContract;
import com.example.android.privatedairy.data.EventDbHelper;

public class EventsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EventDbHelper mDbHelper;
    private static final int  EVENT_LOADER=0;
    EventCursorAdapter  mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Button button=(Button) findViewById(R.id.insert_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EventsActivity.this,EditorActivity.class);
                startActivity(intent);

            }
        });
        mDbHelper =new EventDbHelper(this);

        ListView listView=(ListView) findViewById(R.id.list_events);
        mCursorAdapter =new EventCursorAdapter(this,null);
        listView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(EVENT_LOADER,null,this);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={  EventContract.EventEntry._ID,
              EventContract.EventEntry.COLUMN_EVENT,
                EventContract.EventEntry.COLUMN_TIME,
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
}
