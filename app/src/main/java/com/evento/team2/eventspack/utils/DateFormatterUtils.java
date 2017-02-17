package com.evento.team2.eventspack.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Daniel on 28-Nov-15.
 */
public class DateFormatterUtils {

    public static SimpleDateFormat compareDateFormat;
    public static SimpleDateFormat hoursMinutesDateFormat;
    public static SimpleDateFormat fullDateFormat;

    static {
        compareDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        hoursMinutesDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        fullDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm", Locale.getDefault());
    }
}
