package com.example.android.privatedairy;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.privatedairy.data.EventContract;


public class EventCursorAdapter extends CursorAdapter {
    public EventCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView event = (TextView) view.findViewById(R.id.event_show);
        TextView time= (TextView) view.findViewById(R.id.time_show);

        int eventColumn = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT);
        int timeColoumn= cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DATE);
        String eventDesc=cursor.getString(eventColumn);
        String timeDesc=cursor.getString(timeColoumn);

        event.setText(eventDesc);
        time.setText(timeDesc);

    }
}
