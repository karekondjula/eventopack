package com.evento.team2.eventspack.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.utils.ConversionUtils;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daniel on 15-Aug-15.
 */
public class EventsDatabase {

    private static EventsDatabase instance;
    private SQLiteDatabase database;
    private EventsSqliteHelper dbHelper;
    private Geocoder geocoder;

    private String[] allColumnsEvent = {Event.Table.COLUMN_ID,
            Event.Table.COLUMN_FACEBOOK_ID,
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
            Event.Table.COLUMN_ATTENDING_COUNT,
            Event.Table.COLUMN_CATEGORY_STRING,
            Event.Table.COLUMN_CATEGORY_ID,
    };

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event(cursor.getString(2), cursor.getString(3));
        event.id = cursor.getLong(0);
        event.facebookId = cursor.getLong(1);
        event.pictureUri = cursor.getString(4);
        event.locationString = cursor.getString(5).trim().replace(", null", "").replace("\"", "");
        event.location = new LatLng(cursor.getDouble(6), cursor.getDouble(7));
        event.startTimeStamp = cursor.getLong(8);
        event.startTimeString = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.startTimeStamp);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(event.startTimeStamp);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        event.startDate = cal.getTimeInMillis();

        event.startDateString = cursor.getString(9);//new SimpleDateFormat("dd.MM.yyyy").format(new Date(event.startDate));
        event.endTimeStamp = cursor.getLong(10);
        event.endDateString = event.endTimeStamp != 0 ? DateFormatterUtils.compareDateFormat.format(event.endTimeStamp) : "";
        event.isEventSaved = cursor.getInt(11) == Event.SAVED;

        event.attendingCount = cursor.getString(12);
        event.categoryString = cursor.getString(13);
        event.categoryId = Event.getCategoryByInt(cursor.getInt(14));

        return event;
    }

    private String[] allColumnsPlace = {Place.Table.COLUMN_ID,
            Place.Table.COLUMN_NAME,
            Place.Table.COLUMN_LOCATION_STRING,
            Place.Table.COLUMN_PICTURE_URI,
            Place.Table.COLUMN_LATITUDE,
            Place.Table.COLUMN_LONGITUDE,
    };

    private Place cursorToPlace(Cursor cursor) {
        Place place = new Place();

        place.id = cursor.getLong(0);
        place.name = cursor.getString(1);
        if (!TextUtils.isEmpty(cursor.getString(2))) {
            place.locationString = cursor.getString(2);
        }
        if (!TextUtils.isEmpty(cursor.getString(3))) {
            place.pictureUri = cursor.getString(3);
        }
        place.location = new LatLng(cursor.getDouble(4), cursor.getDouble(5));

        return place;
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

    public void changeSaveEvent(Event event, boolean isSaved) {
        ContentValues values = new ContentValues();
        values.put(Event.Table.COLUMN_IS_EVENT_SAVED, isSaved);
        database.update(Event.Table.TABLE_EVENTS, values, Event.Table.COLUMN_ID + " = ?", new String[]{String.valueOf(event.id)});
    }

//    public void persistPlaces(ArrayList<Place> places) {
//        for (Place place : places) {
//            persistPlace(place);
//        }
//    }

    public long persistPlace(Place place) {
        ContentValues values = new ContentValues();
        values.put(Place.Table.COLUMN_ID, place.id);
        values.put(Place.Table.COLUMN_NAME, place.name);
        values.put(Place.Table.COLUMN_LATITUDE, place.location.latitude);
        values.put(Place.Table.COLUMN_LONGITUDE, place.location.longitude);
        values.put(Place.Table.COLUMN_LOCATION_STRING, place.locationString);
        values.put(Place.Table.COLUMN_PICTURE_URI, place.pictureUri);

        try {
            long updateRows = database.update(Place.Table.TABLE_PLACES,
                    values,
                    Place.Table.COLUMN_ID + " = ? OR " +
                            " ( " +
                            Place.Table.COLUMN_LATITUDE + " = ? AND " +
                            Place.Table.COLUMN_LONGITUDE + " = ? " +
                            " ) ",
                    new String[]{String.valueOf(place.id),
                            String.valueOf(place.location.latitude),
                            String.valueOf(place.location.longitude),
                    });
            if (updateRows == 0) {
                return database.insert(Place.Table.TABLE_PLACES, null, values);
            } else {
                return updateRows;
            }
        } catch (SQLiteConstraintException sqlException) {
            sqlException.printStackTrace();
        }

        return -1;
    }

    public ArrayList<Place> getPlaces(String... filter) {
        ArrayList<Place> placeArrayList = new ArrayList<Place>();

        Cursor cursor = database.query(Place.Table.TABLE_PLACES,
                allColumnsPlace,
                (filter != null && filter.length > 0
                        ? Event.Table.COLUMN_NAME + " LIKE ? OR " +
                        Event.Table.COLUMN_NAME + " LIKE ? "
                        : null),
                (filter != null && filter.length > 0
                        ? new String[]{"%" + ConversionUtils.convertCyrilicToText(filter[0]) + "%",
                        "%" + ConversionUtils.convertTextToCyrilic(filter[0]) + "%",
                }
                        : null),
                null, null, null);

        cursor.moveToFirst();
        Place place;
        while (!cursor.isAfterLast()) {
            place = cursorToPlace(cursor);
            placeArrayList.add(place);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return placeArrayList;
    }

    public long persistEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(Event.Table.COLUMN_ID, event.id);
        values.put(Event.Table.COLUMN_FACEBOOK_ID, event.facebookId);
        values.put(Event.Table.COLUMN_NAME, event.name);
        if (!TextUtils.isEmpty(event.details)) {
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
                        stringBuilder.append(addresses.get(0).getAddressLine(i)).append(", ");
                    }
                    event.locationString = stringBuilder.toString().trim().substring(0, stringBuilder.length() - 2).replace("(FYROM)", "");
                    event.locationString = event.locationString.trim().replace(", null", "").replace("\"", "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // remove trailing commas
            values.put(Event.Table.COLUMN_LOCATION_STRING, event.locationString.replaceAll(", (?!\\p{L})", ""));
        }

        values.put(Event.Table.COLUMN_START_TIME_STAMP, event.startTimeStamp);
        values.put(Event.Table.COLUMN_START_DATE_STRING, event.startDateString);
        values.put(Event.Table.COLUMN_END_TIME_STAMP, event.endTimeStamp);

        values.put(Event.Table.COLUMN_ATTENDING_COUNT, event.attendingCount);

        values.put(Event.Table.COLUMN_CATEGORY_ID, event.categoryId);
        values.put(Event.Table.COLUMN_CATEGORY_STRING, event.categoryString);

        long updateRows = database.update(Event.Table.TABLE_EVENTS,
                values,
                Event.Table.COLUMN_ID + " = ? OR " +
                        Event.Table.COLUMN_FACEBOOK_ID + " = ? ",
                new String[]{String.valueOf(event.id),
                        String.valueOf(event.facebookId),
                }
        );

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

    public ArrayList<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumnsEvent,
                null,
                null,
                null,
                null,
                Event.Table.COLUMN_START_TIME_STAMP + " ASC");

        cursor.moveToFirst();
        Event event;
        while (!cursor.isAfterLast()) {
            event = cursorToEvent(cursor);
//            Log.i(">>", event.toString());
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        if (events.size() == 0) {
            // no events are fetched from server -> empty database
            return null;
        }

        return events;
    }

    public ArrayList<Event> getEvents(String filter, int offset, String dateNow) {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.rawQuery("SELECT * " +
                        " FROM " + Event.Table.TABLE_EVENTS +
                        " WHERE ( " + Event.Table.COLUMN_NAME + " LIKE ? OR " +
                                    Event.Table.COLUMN_NAME + " LIKE ? OR " +
                                    Event.Table.COLUMN_DETAILS + " LIKE ? OR " +
                                    Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                                    Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                                    Event.Table.COLUMN_START_DATE_STRING + " LIKE ? " +
                        " ) " +
                        "   AND ( " + Event.Table.COLUMN_START_TIME_STAMP + " > ? " +
                        "           OR " + Event.Table.COLUMN_START_TIME_STAMP + " < ? AND " + Event.Table.COLUMN_END_TIME_STAMP + " > ? " +
                        "       ) " +
                        " ORDER BY " + Event.Table.COLUMN_START_TIME_STAMP + " ASC " +
                        " LIMIT ?",
                new String[]{"%" + ConversionUtils.convertTextToCyrilic(filter) + "%",
                        "%" + ConversionUtils.convertCyrilicToText(filter) + "%",
                        filter,
                        "%" + ConversionUtils.convertTextToCyrilic(filter) + "%",
                        "%" + ConversionUtils.convertCyrilicToText(filter) + "%",
                        filter,
                        dateNow,
                        dateNow,
                        dateNow,
                        String.valueOf(offset)}
        );

        cursor.moveToFirst();
        Event event;
        while (!cursor.isAfterLast()) {
            event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        if (events.size() == 0 && filter != null && TextUtils.isEmpty(filter)) {
            // no events are fetched from server -> empty database
            return null;
        }

        return events;
    }

    public ArrayList<Event> getActiveEventsOnDate(String timestamp) {
        ArrayList<Event> events = new ArrayList<Event>();

        StringBuilder where;
        String whereArgs[];
        ArrayList<String> whereArgsList = new ArrayList<String>();

        where = new StringBuilder();
        where.append(" ( " +
                "( " + Event.Table.COLUMN_START_TIME_STAMP + " - ? < 86400000 AND " + Event.Table.COLUMN_START_TIME_STAMP + " - ? >= 0 " + ") " +
                " OR " +
                "( " + Event.Table.COLUMN_START_TIME_STAMP + " - ? <= 0 AND " + Event.Table.COLUMN_END_TIME_STAMP + " > ? ) " +
                " ) "
        );
        whereArgsList.add(timestamp);
        whereArgsList.add(timestamp);
        whereArgsList.add(timestamp);
        whereArgsList.add(timestamp);

        whereArgs = new String[whereArgsList.size()];
        whereArgs = whereArgsList.toArray(whereArgs);

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumnsEvent,
                where.toString(),
                whereArgs,
                null,
                null,
                null);

        cursor.moveToFirst();
        Event event;
        while (!cursor.isAfterLast()) {
            event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public ArrayList<Event> getActiveEventsByLocation(String... filter) {
        ArrayList<Event> events = new ArrayList<>();

        StringBuilder where = null;
        String whereArgs[] = null;
        ArrayList<String> whereArgsList = new ArrayList<>();
        if (filter != null) {
            where = new StringBuilder();
            if (filter.length > 0) {
                where.append("( " +
                        "(" + Event.Table.COLUMN_LATITUDE + " LIKE ? AND " +
                        Event.Table.COLUMN_LONGITUDE + " LIKE ? ) AND " +
                        Event.Table.COLUMN_LOCATION_STRING + " LIKE ? ) " +
                        " AND ( " +
                        Event.Table.COLUMN_START_TIME_STAMP + " > ? " +
                        " OR " +
                        "( " + Event.Table.COLUMN_START_TIME_STAMP + " < ? AND " + Event.Table.COLUMN_END_TIME_STAMP + " > ? ) " +
                        ")"
                );

                whereArgsList.add("%" + filter[0] + "%");
                whereArgsList.add("%" + filter[1] + "%");
                whereArgsList.add("%" + filter[2] + "%");
                whereArgsList.add(filter[3]);
                whereArgsList.add(filter[3]);
                whereArgsList.add(filter[3]);
            }

            whereArgs = new String[whereArgsList.size()];
            whereArgs = whereArgsList.toArray(whereArgs);
        }

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumnsEvent,
                (where != null ? where.toString() : null),
                (whereArgs != null ? whereArgs : null),
                null,
                null,
                Event.Table.COLUMN_START_TIME_STAMP + " ASC");

        cursor.moveToFirst();
        Event event;
        while (!cursor.isAfterLast()) {
            event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    public ArrayList<Event> getActiveEventsByCategory(@Event.Category int categoryId, String... filter) {
        ArrayList<Event> events = new ArrayList<>();

        StringBuilder where = new StringBuilder();
        String whereArgs[] = null;
        ArrayList<String> whereArgsList = new ArrayList<>();

        where.append(Event.Table.COLUMN_CATEGORY_ID + " = ? ");
        whereArgsList.add(String.valueOf(categoryId));

        if (filter != null) {
            if (filter.length > 0) {
                where.append(" AND ( " +
                        Event.Table.COLUMN_START_TIME_STAMP + " > ? " +
                        " OR " +
                        "( " + Event.Table.COLUMN_START_TIME_STAMP + " < ? AND " + Event.Table.COLUMN_END_TIME_STAMP + " > ? ) " +
                        ")");
                whereArgsList.add(filter[0]);
                whereArgsList.add(filter[0]);
                whereArgsList.add(filter[0]);
            }
            if (filter.length > 1) {
                where.append(" AND (" + Event.Table.COLUMN_NAME + " LIKE ? OR " +
                        Event.Table.COLUMN_NAME + " LIKE ? OR " +
                        Event.Table.COLUMN_DETAILS + " LIKE ? OR " +
                        Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                        Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                        Event.Table.COLUMN_START_DATE_STRING + " LIKE ? ) ");

                whereArgsList.add("%" + ConversionUtils.convertCyrilicToText(filter[1]) + "%");
                whereArgsList.add("%" + ConversionUtils.convertTextToCyrilic(filter[1]) + "%");
                whereArgsList.add("%" + filter[1] + "%");
                whereArgsList.add("%" + ConversionUtils.convertTextToCyrilic(filter[1]) + "%");
                whereArgsList.add("%" + ConversionUtils.convertCyrilicToText(filter[1]) + "%");
                whereArgsList.add("%" + filter[1] + "%");
            }

            whereArgs = new String[whereArgsList.size()];
            whereArgs = whereArgsList.toArray(whereArgs);
        }

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumnsEvent,
                where.toString(),
                whereArgs,
                null,
                null,
                Event.Table.COLUMN_START_TIME_STAMP + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
//            Log.i(">>", DateFormatterUtils.fullDateFormat.format(new Date(event.startTimeStamp)) + " - " + event.name);
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    // TODO refactor it in better times (no need for two differet get<>Events methods!!!
    public ArrayList<Event> getSavedEvents(String... filter) {
        ArrayList<Event> events = new ArrayList<>();

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumnsEvent,
                Event.Table.COLUMN_IS_EVENT_SAVED + " = ? " +
                        (filter != null && filter.length > 0 ? " AND (" +
                                Event.Table.COLUMN_NAME + " LIKE ? OR " +
                                Event.Table.COLUMN_NAME + " LIKE ? OR " +
                                Event.Table.COLUMN_DETAILS + " LIKE ? OR " +
                                Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                                Event.Table.COLUMN_LOCATION_STRING + " LIKE ? OR " +
                                Event.Table.COLUMN_START_DATE_STRING + " LIKE ? )"
                                : ""),
                (filter != null && filter.length > 0 ? new String[]{String.valueOf(Event.SAVED),
                        "%" + ConversionUtils.convertCyrilicToText(filter[0]) + "%",
                        "%" + ConversionUtils.convertTextToCyrilic(filter[0]) + "%",
                        "%" + filter[0] + "%",
                        "%" + ConversionUtils.convertCyrilicToText(filter[0]) + "%",
                        "%" + ConversionUtils.convertTextToCyrilic(filter[0]) + "%",
                        "%" + filter[0] + "%",}
                        : new String[]{String.valueOf(Event.SAVED)}),
                null,
                null,
                Event.Table.COLUMN_START_TIME_STAMP + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
//            Log.i(">>", event.toString());
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    public ArrayList<Event> getSavedEventsInNext24Hours(String timestamp) {
        ArrayList<Event> events = new ArrayList<Event>();

        StringBuilder where;
        String whereArgs[];
        ArrayList<String> whereArgsList = new ArrayList<String>();

        where = new StringBuilder();
        where.append(Event.Table.COLUMN_IS_EVENT_SAVED + " = ? " +
                " AND " + Event.Table.COLUMN_START_TIME_STAMP + " - ? > 0 " +
                " AND " + Event.Table.COLUMN_START_TIME_STAMP + " - ? <= 86400000 "
        );
        whereArgsList.add(String.valueOf(Event.SAVED));
        whereArgsList.add(timestamp);
        whereArgsList.add(timestamp);

        whereArgs = new String[whereArgsList.size()];
        whereArgs = whereArgsList.toArray(whereArgs);

        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumnsEvent,
                where.toString(),
                whereArgs,
                null,
                null,
                null);

        cursor.moveToFirst();
        Event event;
        while (!cursor.isAfterLast()) {
            event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public Event getEventById(long eventId) {
        Cursor cursor = database.query(Event.Table.TABLE_EVENTS,
                allColumnsEvent, Event.Table.COLUMN_ID + " = ? ", new String[]{String.valueOf(eventId)},
                null, null, null);

        Event event = null;
        if (cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();
            event = cursorToEvent(cursor);
            cursor.close();
        }

        return event;
    }

    public Place getPlaceById(long placeId) {
        Cursor cursor = database.query(Place.Table.TABLE_PLACES,
                allColumnsPlace, Place.Table.COLUMN_ID + " = ? ", new String[]{String.valueOf(placeId)},
                null, null, null);
        cursor.moveToFirst();
        Place place = cursorToPlace(cursor);
        cursor.close();

        return place;
    }

    public void cleanUpEventsAndPlaces() {
//        database.execSQL("DELETE FROM " + Place.Table.TABLE_PLACES);

        // delete events older than two months
        database.execSQL("DELETE FROM " + Event.Table.TABLE_EVENTS +
                        " WHERE " + Event.Table.COLUMN_START_TIME_STAMP + " < ?",
                new String[]{String.valueOf((new Date().getTime() - 5184000000L))}
        );
    }
}