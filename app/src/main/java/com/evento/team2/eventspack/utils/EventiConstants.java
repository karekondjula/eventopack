package com.evento.team2.eventspack.utils;

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
}