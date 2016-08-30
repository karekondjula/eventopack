package com.evento.team2.eventspack.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Daniel on 28-Nov-15.
 */
public class DateFormatterUtils {

    public static SimpleDateFormat compareDateFormat;
    public static SimpleDateFormat hoursMinutesDateFormat;
    public static SimpleDateFormat fullDateFormat;

    static {
        compareDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        hoursMinutesDateFormat = new SimpleDateFormat("HH:mm");
        fullDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
    }
}
