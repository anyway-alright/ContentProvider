package com.example.android.privatedairy.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class EventContract implements BaseColumns {
    public static final String CONTENT_AUTHORITY="com.example.android.privatedairy";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_EVENTS = "events";


    public static final class  EventEntry{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_EVENTS);

        public final static String TABLE_NAME = "events";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_EVENT= "event";
        public final static String COLUMN_TIME= "time";
        public final static String COLUMN_DATE= "date";
        public final static String COLUMN_NOTE="note";
    }


}
