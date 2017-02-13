package com.example.android.privatedairy;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.privatedairy.data.EventContract;

public class EditorActivity extends AppCompatActivity {
    private EditText eventEdit;
    private EditText timeEdit;
    private Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        eventEdit = (EditText) findViewById(R.id.event_desc);
        timeEdit = (EditText) findViewById(R.id.time_desc);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
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
                // Do nothing for
                // showDeleteConfirmationDialog();
                return true;
        }
            return super.onOptionsItemSelected(item);
        }


    private void savePet() {
        String eventString = eventEdit.getText().toString().trim();
        String timeString = timeEdit.getText().toString().trim();


        if (currentUri == null &&
                TextUtils.isEmpty(eventString) && TextUtils.isEmpty(timeString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_EVENT, eventString);
        values.put(EventContract.EventEntry.COLUMN_TIME, timeString);

            Uri newUri = getContentResolver().insert(EventContract.EventEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "Faied",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Success",
                        Toast.LENGTH_SHORT).show();
            }
    }


}
