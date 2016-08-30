package com.evento.team2.eventspack.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;

/**
 * Created by Daniel on 15-Aug-15.
 */
public class EventsSqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 3;

    private static int currentVersion = 2;

    public EventsSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        upgradeDatabase(database, 0, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        upgradeDatabase(database, ++currentVersion, DATABASE_VERSION);
    }

    private void upgradeDatabase(SQLiteDatabase database, int fromVersion, int toVersion) {
        while (fromVersion <= toVersion) {
            if (fromVersion == 1) {
                database.execSQL(Event.Table.TABLE_EVENTS_CREATE);
                database.execSQL(Place.Table.TABLE_PLACES_CREATE);
            } else if (fromVersion == 2) {
                database.execSQL("ALTER TABLE " + Event.Table.TABLE_EVENTS + " ADD " + Event.Table.COLUMN_ATTENDING_COUNT + " TEXT" + ";");
                database.execSQL("ALTER TABLE " + Event.Table.TABLE_EVENTS + " ADD " + Event.Table.COLUMN_CATEGORY_STRING + " TEXT" + ";");
            } else if (fromVersion == 3) {
                database.execSQL("ALTER TABLE " + Event.Table.TABLE_EVENTS + " ADD " + Event.Table.COLUMN_CATEGORY_ID + " integer" + ";");
            }

            fromVersion++;
        }

        currentVersion = DATABASE_VERSION;
    }
}