package com.evento.team2.eventspack.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.evento.team2.eventspack.model.Event;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 15-Aug-15.
 */
public class EventsDatabase {

    private static EventsDatabase instance;

    private SQLiteDatabase database;
    private EventsSqliteHelper dbHelper;
    private String[] allColumns = {Event.Table.COLUMN_ID,
            Event.Table.COLUMN_NAME,
            Event.Table.COLUMN_DETAILS,
            Event.Table.COLUMN_PICTURE_URI,
            Event.Table.COLUMN_LOCATION_STRING,
            Event.Table.COLUMN_LATITUDE,
            Event.Table.COLUMN_LONGITUDE,
            Event.Table.COLUMN_START_DATE,
            Event.Table.COLUMN_END_DATE,};

    private EventsDatabase() {
    }

    public static EventsDatabase getInstance() {
        if (instance == null) {
            instance = new EventsDatabase();
        }

        return instance;
    }

    public void openEventsDatabase(Context context) throws SQLException {
        if (dbHelper == null) {
            dbHelper = new EventsSqliteHelper(context);
        }

        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public void saveEvents(ArrayList<Event> events) {
        for (Event event : events) {
            saveEvent(event);
        }
    }

    public long saveEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(Event.Table.COLUMN_ID, event.id);
        values.put(Event.Table.COLUMN_NAME, event.name);
        if (event.details != null) {
            values.put(Event.Table.COLUMN_DETAILS, event.details);
        }
        values.put(Event.Table.COLUMN_PICTURE_URI, event.pictureUri);
        values.put(Event.Table.COLUMN_LOCATION_STRING, event.locationString);
        if (event.location != null) {
            values.put(Event.Table.COLUMN_LATITUDE, event.location.latitude);
            values.put(Event.Table.COLUMN_LONGITUDE, event.location.longitude);
        }
        values.put(Event.Table.COLUMN_START_DATE, event.startDate);
        values.put(Event.Table.COLUMN_END_DATE, event.endDate);

        long updateRows = database.update(Event.Table.TABLE_EVENTS, values, Event.Table.COLUMN_ID + " = ?", new String[]{String.valueOf(event.id)});

        if (updateRows == 0) {
            long insertId = database.insert(Event.Table.TABLE_EVENTS, null, values);
            return insertId;
        } else {
            return updateRows;
        }
    }

    public void removeSavedEvent(Event event) {
        long id = event.id;
        database.delete(Event.Table.TABLE_EVENTS, Event.Table.COLUMN_ID + " = " + id, null);
    }

    public ArrayList<Event> getSavedEvents(String filter) {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumns,
                Event.Table.COLUMN_NAME + " LIKE ? OR " +
                Event.Table.COLUMN_DETAILS + " LIKE ? OR " +
                Event.Table.COLUMN_LOCATION_STRING + " LIKE ? ",
                new String[]{"%" + filter + "%", "%" + filter + "%", "%" + filter + "%"},
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            event.isEventSaved = true;
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public Event getSavedEventById(long id) {
        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumns, Event.Table.COLUMN_ID + " = ? ", new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        Event event = cursorToEvent(cursor);
        cursor.close();

        return event;
    }

    // TODO daniel update this method with all the new fields
    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event("", "");
        event.id = cursor.getLong(0);
        event.name = cursor.getString(1);
        event.details = cursor.getString(2);
        event.pictureUri = cursor.getString(3);
        event.location = new LatLng(cursor.getDouble(4), cursor.getDouble(5));
        event.startDate = cursor.getLong(6);
        event.startDateString = new SimpleDateFormat("HH:mm dd.MM.yyyy ").format(new Date(event.startDate));
        return event;
    }
}