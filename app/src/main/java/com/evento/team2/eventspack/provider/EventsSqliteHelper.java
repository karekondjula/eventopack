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
    private static final int DATABASE_VERSION = 2; // 1

    private static int currentVersion = 1;

    public EventsSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        upgradeDatabase(database, 1, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//        onCreate(database);
        upgradeDatabase(database, ++currentVersion, DATABASE_VERSION);
    }

    private void upgradeDatabase(SQLiteDatabase database, int fromVersion, int toVersion) {
        while (fromVersion <= toVersion) {
            if (fromVersion == 1) {
                database.execSQL(Event.Table.TABLE_EVENTS_CREATE);
                database.execSQL(Place.Table.TABLE_PLACES_CREATE);
            } else if (fromVersion == 2) {
                // add category and attending_count columnds
                database.execSQL("ALTER TABLE " + Event.Table.TABLE_EVENTS + " ADD " + Event.Table.COLUMN_ATTENDING_COUNT + " TEXT" + ";");
                database.execSQL("ALTER TABLE " + Event.Table.TABLE_EVENTS + " ADD " + Event.Table.COLUMN_CATEGORY_STRING + " TEXT" + ";");
            }

            fromVersion++;
        }

        currentVersion = DATABASE_VERSION;
    }
}