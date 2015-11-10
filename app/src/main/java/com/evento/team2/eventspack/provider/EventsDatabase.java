package com.evento.team2.eventspack.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import com.evento.team2.eventspack.model.Event;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 15-Aug-15.
 */
public class EventsDatabase {

    private static EventsDatabase instance;

    private SQLiteDatabase database;
    private EventsSqliteHelper dbHelper;

    private Geocoder geocoder;

    private String[] allColumns = {Event.Table.COLUMN_ID,
            Event.Table.COLUMN_NAME,
            Event.Table.COLUMN_DETAILS,
            Event.Table.COLUMN_PICTURE_URI,
            Event.Table.COLUMN_LOCATION_STRING,
            Event.Table.COLUMN_LATITUDE,
            Event.Table.COLUMN_LONGITUDE,
            Event.Table.COLUMN_START_TIME_STAMP,
            Event.Table.COLUMN_START_DATE_STRING,
            Event.Table.COLUMN_END_TIME_STAMP,
            Event.Table.COLUMN_IS_EVENT_SAVED,
    };


    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event(cursor.getString(1), cursor.getString(2));
        event.id = cursor.getLong(0);
        event.pictureUri = cursor.getString(3);
        event.locationString = cursor.getString(4);
        event.location = new LatLng(cursor.getDouble(5), cursor.getDouble(6));
        event.startTimeStamp = cursor.getLong(7);
        event.startTimeString = new SimpleDateFormat("HH:mm").format(new Date(event.startTimeStamp));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(event.startTimeStamp);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        event.startDate = cal.getTimeInMillis();

        event.startDateString = cursor.getString(8);//new SimpleDateFormat("dd.MM.yyyy").format(new Date(event.startDate));
        event.endTimeStamp = cursor.getLong(9);
        event.endDateString = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date(event.endTimeStamp));
        event.isEventSaved = cursor.getInt(10) == Event.SAVED ? true : false;
        return event;
    }

    private EventsDatabase() {
    }

    public static EventsDatabase getInstance() {
        if (instance == null) {
            instance = new EventsDatabase();
        }

        return instance;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
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

    public void changeSaveEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(Event.Table.COLUMN_IS_EVENT_SAVED, event.isEventSaved ? Event.SAVED : Event.NOT_SAVED);
        database.update(Event.Table.TABLE_EVENTS, values, Event.Table.COLUMN_ID + " = ?", new String[]{String.valueOf(event.id)});
    }

    public void persistEvents(ArrayList<Event> events) {
        for (Event event : events) {
            persistEvent(event);
        }
    }

    public long persistEvent(Event event) {
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

            if (TextUtils.isEmpty(event.locationString)) {
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(event.location.latitude, event.location.longitude, 1);
                    Address address = addresses.get(addresses.size() - 1);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        stringBuilder.append(addresses.get(0).getAddressLine(i) + ", ");
                    }
                    event.locationString = stringBuilder.toString().trim().substring(0, stringBuilder.length() - 2).replace("(FYROM)", "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            values.put(Event.Table.COLUMN_LOCATION_STRING, event.locationString);
        }
        values.put(Event.Table.COLUMN_START_TIME_STAMP, event.startTimeStamp);
        values.put(Event.Table.COLUMN_START_DATE_STRING, event.startDateString);
        values.put(Event.Table.COLUMN_END_TIME_STAMP, event.endTimeStamp);

        long updateRows = database.update(Event.Table.TABLE_EVENTS, values, Event.Table.COLUMN_ID + " = ?", new String[]{String.valueOf(event.id)});

        if (updateRows == 0) {
            values.put(Event.Table.COLUMN_IS_EVENT_SAVED, Event.NOT_SAVED);
            return database.insert(Event.Table.TABLE_EVENTS, null, values);
        } else {
            return updateRows;
        }
    }

//    public void removeSavedEvent(Event event) {
//        long id = event.id;
//        database.delete(Event.Table.TABLE_EVENTS, Event.Table.COLUMN_ID + " = " + id, null);
//    }

    public ArrayList<Event> getEvents(String... filter) {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumns,
                (filter != null && filter.length > 0
                        ? Event.Table.COLUMN_NAME + " LIKE ? OR " +
                        Event.Table.COLUMN_DETAILS + " LIKE ? OR " +
                        Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                        Event.Table.COLUMN_START_DATE_STRING + " LIKE ? "
                        : null),
                (filter != null && filter.length > 0
                        ? new String[]{"%" + filter[0] + "%",
                        "%" + filter[0] + "%",
                        "%" + filter[0] + "%",
                        "%" + filter[0] + "%",}
                        : null),
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    // TODO refactor it in better times (no need for two differet get<>Events methods!!!
    public ArrayList<Event> getSavedEvents(String... filter) {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumns,
                Event.Table.COLUMN_IS_EVENT_SAVED + " = ? " +
                        (filter != null  && filter.length > 0 ? " AND (" +
                                Event.Table.COLUMN_NAME + " LIKE ? OR " +
                                Event.Table.COLUMN_DETAILS + " LIKE ? OR " +
                                Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                                Event.Table.COLUMN_START_DATE_STRING + " LIKE ? )"
                                : ""),
                (filter != null && filter.length > 0 ? new String[]{String.valueOf(Event.SAVED),
                        "%" + filter[0] + "%",
                        "%" + filter[0] + "%",
                        "%" + filter[0] + "%",
                        "%" + filter[0] + "%",
                        "%" + filter[0] + "%",}
                        : new String[]{String.valueOf(Event.SAVED)}),
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public Event getEventById(long id) {
        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumns, Event.Table.COLUMN_ID + " = ? ", new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        Event event = cursorToEvent(cursor);
        cursor.close();

        return event;
    }
}