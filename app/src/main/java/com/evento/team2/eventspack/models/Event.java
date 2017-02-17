package com.evento.team2.eventspack.models;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Daniel on 04-Aug-15.
 */
// TODO try AutoValues
//https://caster.io/episodes/understanding-autovalue-autovalue-value-types-minus-boiler-plate-part-1
public class Event implements Comparable<Event> {

    @IntDef({FUN, CINEMA, CULTURE, FESTIVAL, PROMOTION, SPORT, FAIR, EDUCATION, CONCERTS, OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
    }

    public static final int FUN = 1;
    public static final int CINEMA = 2;
    public static final int CULTURE = 3;
    public static final int FESTIVAL = 4;
    public static final int PROMOTION = 5;
    public static final int SPORT = 6;
    public static final int FAIR = 7;
    public static final int EDUCATION = 8;
    public static final int CONCERTS = 9;
    public static final int OTHER = 100;

    public static final int NOT_SAVED = 0;
    public static final int SAVED = 1;

    public static final int NOT_DELETED = 0;
    public static final int DELETED = 1;
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
    @Category
    public int categoryId;
    public String categoryString;
    public int isDeleted = NOT_DELETED;

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

    public static @Category int getCategoryByInt(int categoryId) {
        switch (categoryId) {
            case FUN:
                return FUN;
            case CINEMA:
                return CINEMA;
            case CULTURE:
                return CULTURE;
            case FESTIVAL:
                return FESTIVAL;
            case PROMOTION:
                return PROMOTION;
            case SPORT:
                return SPORT;
            case FAIR:
                return FAIR;
            case EDUCATION:
                return EDUCATION;
            case CONCERTS:
                return CONCERTS;
            case OTHER:
                return OTHER;
            default:
                return OTHER;
        }
    }

    @Override
    public int compareTo(@NonNull Event event) {
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
            return id == otherEvent.id
                    && name.equals(otherEvent.name)
                    && details.equals(otherEvent.details)
                    && location.equals(otherEvent.location)
                    && startTimeStamp == otherEvent.startTimeStamp
                    && isEventSaved == otherEvent.isEventSaved;
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
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_CATEGORY_STRING = "categoryString";
        public static final String COLUMN_IS_DELETED = "isDeleted";

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