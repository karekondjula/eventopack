package com.evento.team2.eventspack.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.model.Place;

/**
 * Created by Daniel on 15-Aug-15.
 */
public class EventsSqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    public EventsSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Event.Table.TABLE_EVENTS_CREATE);
        database.execSQL(Place.Table.TABLE_PLACES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//        Log.w(EventsSqliteHelper.class.getName(),
//                "Upgrading database from version " + oldVersion + " to "
//                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + Event.Table.TABLE_EVENTS);
        database.execSQL("DROP TABLE IF EXISTS " + Place.Table.TABLE_PLACES);
        onCreate(database);
    }

}