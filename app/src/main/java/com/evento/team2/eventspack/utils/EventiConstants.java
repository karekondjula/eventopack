package com.evento.team2.eventspack.utils;

import android.Manifest;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Daniel on 29-Oct-15.
 */
public class EventiConstants {

    @IntDef({NONE, EVENTS, PLACES, SAVED_EVENTS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectedCategory {
    }

    public static final int NONE = -1;
    public static final int EVENTS = 0;
    public static final int PLACES = 1;
    public static final int SAVED_EVENTS = 2;

    public static final int NO_EVENT_ID = -1;

    public static final String NO_FILTER_STRING = "";

    // causes a problem with the image of some events
    public static final String TRANSITION_EVENT_IMAGE = "transitionEventImage";

    public static final int PERMISSIONS_REQUEST_CODE = 305;

    public static final String[] ungrantedPremissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
}