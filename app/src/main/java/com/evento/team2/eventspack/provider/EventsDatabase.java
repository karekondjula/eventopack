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
import java.util.List;

/**
 * Created by Daniel on 15-Aug-15.
 */
public class EventsDatabase {

    private static EventsDatabase instance;

    private SQLiteDatabase database;
    private EventsSqliteHelper dbHelper;
    private String[] allColumns = {EventsTable.COLUMN_ID,
            EventsTable.COLUMN_NAME,
            EventsTable.COLUMN_DETAILS,
            EventsTable.COLUMN_PICTURE_URI,
            EventsTable.COLUMN_LATITUDE,
            EventsTable.COLUMN_LONGITUDE,
            EventsTable.COLUMN_DATE};

    private EventsDatabase() {}

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

    public long saveEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(EventsTable.COLUMN_ID, event.id);
        values.put(EventsTable.COLUMN_NAME, event.name);
        if (event.details != null) {
            values.put(EventsTable.COLUMN_DETAILS, event.details);
        }
        values.put(EventsTable.COLUMN_PICTURE_URI, event.pictureUri);
        if (event.location != null) {
            values.put(EventsTable.COLUMN_LATITUDE, event.location.latitude);
            values.put(EventsTable.COLUMN_LONGITUDE, event.location.longitude);
        }
        values.put(EventsTable.COLUMN_DATE, event.date);

        long insertId = database.insert(EventsTable.TABLE_EVENTS, null, values);
        return insertId;
    }

    public void removeSavedEvent(Event event) {
        long id = event.id;
        database.delete(EventsTable.TABLE_EVENTS, EventsTable.COLUMN_ID + " = " + id, null);
    }

    public ArrayList<Event> getAllSavedEvents() {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(EventsTable.TABLE_EVENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToComment(cursor);
            event.isEventSaved = true;
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public Event getSavedEventById(long id) {
        Cursor cursor = database.query(EventsTable.TABLE_EVENTS,
                allColumns, EventsTable.COLUMN_ID + " = ? ", new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        Event event = cursorToComment(cursor);
        cursor.close();

        return event;
    }

    private Event cursorToComment(Cursor cursor) {
        Event event = new Event("", "");
        event.id = cursor.getLong(0);
        event.name = cursor.getString(1);
        event.details = cursor.getString(2);
        event.pictureUri = cursor.getString(3);
        event.location = new LatLng(cursor.getDouble(4), cursor.getDouble(5));
        event.date = cursor.getLong(6);
        event.dateString = new SimpleDateFormat("HH:mm dd.MM.yyyy ").format(new Date(event.date));
        return event;
    }
}