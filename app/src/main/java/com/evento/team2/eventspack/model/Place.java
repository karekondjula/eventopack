package com.evento.team2.eventspack.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Daniel on 29-Oct-15.
 */
public class Place {

    public static final int NOT_SAVED = 0;
    public static final int SAVED = 1;

    public long id;
    public String name;
    public String details;
    public String pictureUri;
    public LatLng location;
    public String locationString;
    public String workTime;
    public String workDays;

    public final static class Table {
        public static final String TABLE_EVENTS = "Events";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_PICTURE_URI = "pictureUri";
        public static final String COLUMN_LOCATION_STRING = "locationString";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_WORK_TIME = "workTime";
        public static final String COLUMN_WORK_DAYS = "workDays";

        // Database creation sql statement
        public static final String DATABASE_CREATE = "create table " + TABLE_EVENTS + "("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_NAME + " text not null, "
                + COLUMN_DETAILS + " text, "
                + COLUMN_PICTURE_URI + " text, "
                + COLUMN_LOCATION_STRING + " text, "
                + COLUMN_LATITUDE + " double, "
                + COLUMN_LONGITUDE + " double, "
                + COLUMN_WORK_TIME + " text, "
                + COLUMN_WORK_DAYS + " text "
                + ");";
    }
}
