package com.evento.team2.eventspack.models;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Daniel on 04-Aug-15.
 */
public class Event implements Comparable<Event> {

    @IntDef({FUN, CULTURE, SPORT, OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
    }

    public static final int FUN = 0;
    public static final int CULTURE = 1;
    public static final int SPORT = 2;
    public static final int OTHER = 3;

    public static final int NOT_SAVED = 0;
    public static final int SAVED = 1;

    /**
     * Universal id for the event, same on all devices
     */
    public long id;
    public long facebookId;
    public String name;
    public String details;
    public String pictureUri;
    public LatLng location;
    public String locationString;
    public long startTimeStamp;
    public long startDate;
    public long endTimeStamp;
    /**
     * Format dd.MM.yyyy
     */
    public String startDateString;
    public String startTimeString;
    public String endDateString;
    public boolean isEventSaved = false;
    public String attendingCount;
    public int categoryId;
    public String categoryString;

    // TODO adapt this to categoryString
    @Category
    public int category;

    public Event() {
    }

    public Event(String name, @Nullable String description) {
        this.name = (!TextUtils.isEmpty(name) && !name.equals("NULL")) ? name : "";
        this.details = (!TextUtils.isEmpty(description) && !description.equals("NULL")) ? description : "";
    }

    public Event(String name, String description, @Nullable String pictureUri) {
        this.name = name;
        this.details = description;
        this.pictureUri = pictureUri;
    }

    @Override
    public int compareTo(Event event) {
        if (id == event.id) {
            return 0;
        } else if (id > event.id) {

            if (startTimeStamp > event.startTimeStamp) {
                return 1;
            }
            if (startTimeStamp == event.startTimeStamp) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Event) {
            Event otherEvent = (Event) o;
            if (id == otherEvent.id
                    && name.equals(otherEvent.name)
                    && details.equals(otherEvent.details)
                    && location.equals(otherEvent.location)
                    && startTimeStamp == otherEvent.startTimeStamp
                    && isEventSaved == otherEvent.isEventSaved) {
                return true;
            }
            return false;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "name: " + name + "\n " +
                "id: " + id + "\n " +
//                "facebook id: " + facebookId + "\n " +
//                "details:" + details + "\n " +
//                "pictureUri: " + pictureUri + "\n " +
//                "location: " + location + "\n " +
//                "locationString " + locationString + "\n " +
                "startTimeStamp " + startTimeStamp + "\n " +
                "startDate " + startDate + "\n " +
                "endTimeStamp " + endTimeStamp + "\n " +
                "startDateString " + startDateString + "\n " +
//                "startTimeString " + startTimeString + "\n " +
//                "endDateString " + endDateString + "\n " +
//                "isEventSaved: " + isEventSaved + "\n" +
//                "attendingCount: " + attendingCount + "\n" +
//                "categoryString: " + categoryString +
                "\n"
                ;
    }

    public final static class Table {
        public static final String TABLE_EVENTS = "Events";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_FACEBOOK_ID = "facebookId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_PICTURE_URI = "pictureUri";
        public static final String COLUMN_LOCATION_STRING = "locationString";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_START_TIME_STAMP = "startTimeStamp";
        public static final String COLUMN_START_DATE_STRING = "startDateString";
        public static final String COLUMN_END_TIME_STAMP = "endTimeStamp";
        public static final String COLUMN_IS_EVENT_SAVED = "isEventSaved";
        public static final String COLUMN_ATTENDING_COUNT = "attendingCount";
        public static final String COLUMN_CATEGORY_STRING = "categoryString";

        // Database creation sql statement
        public static final String TABLE_EVENTS_CREATE = "create table " + TABLE_EVENTS + "("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_FACEBOOK_ID + " integer, "
                + COLUMN_NAME + " text not null, "
                + COLUMN_DETAILS + " text, "
                + COLUMN_PICTURE_URI + " text, "
                + COLUMN_LOCATION_STRING + " text, "
                + COLUMN_LATITUDE + " double, "
                + COLUMN_LONGITUDE + " double, "
                + COLUMN_START_TIME_STAMP + " long, "
                + COLUMN_START_DATE_STRING + " text, "
                + COLUMN_END_TIME_STAMP + " long, "
                + COLUMN_IS_EVENT_SAVED + " integer "
                + ");";
    }
}