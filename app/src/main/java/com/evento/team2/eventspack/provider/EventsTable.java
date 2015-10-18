package com.evento.team2.eventspack.provider;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Daniel on 15-Aug-15.
 */
public class EventsTable {

    public static final String TABLE_EVENTS = "Events";

    // TODO daniel we have three different classes for Events, is it stupid?!?!

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DETAILS = "details";
    public static final String COLUMN_PICTURE_URI = "pictureUri";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DATE = "startDate";

    // Database creation sql statement
    public static final String DATABASE_CREATE = "create table " + TABLE_EVENTS + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DETAILS + " text, "
            + COLUMN_PICTURE_URI + " text, "
            + COLUMN_LATITUDE + " real, "
            + COLUMN_LONGITUDE + " real, "
            + COLUMN_DATE + " long "
            + ");";
}
