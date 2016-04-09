package com.evento.team2.eventspack.utils;

import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.soapservice.model.JsonEvent;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;

/**
 * Created by Daniel on 17-Oct-15.
 */
public class ConversionUtils {

    private static String[] listLat = null;
    private static String[] listCyr = null;

    static {
        // TODO sooon >)
//        String lat = "DZ LJ NJ A B C CH D E F G H I J K L M N O P R S SH T U V Z ZH";
//        String lat = "LJ NJ A B CH D E F G H I J K L M N O P R S SH T U V ZH";
//        lat += " " + lat.toLowerCase();
//        lat += " Lj Nj";
//
//        listLat = lat.split(" ");
//        listCyr = cyr.split(" ");
    }

    public static String convertTextToCyrilic(String line) {

        int i = 0;

        return line.replaceAll("c|ch|g|gj|s|sh", "%");
//        for (String item : listLat) {
//            line = line.replaceAll(item, listCyr[i]);
//            i++;
//        }
//
//        return line;
    }

    public static Event convertJsonEventToEvent(JsonEvent jsonEvent) {
        Event event = new Event(jsonEvent.title, jsonEvent.teaser);

        event.id = jsonEvent.id;
        event.facebookId = jsonEvent.fb_id;
        event.startTimeStamp = jsonEvent.startTime * 1000; // in ms

        String dateTime = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(event.startTimeStamp);

        event.startTimeString = dateTime.split(" ")[0];
        event.startDateString = dateTime.split(" ")[1];

        event.endTimeStamp = jsonEvent.endTime * 1000; // in ms
        event.pictureUri = jsonEvent.image_url;
        event.locationString = jsonEvent.location.concat(", " + jsonEvent.city);
        event.location = new LatLng(jsonEvent.lat, jsonEvent.lng);

        event.attendingCount = jsonEvent.attending_count;
        event.categoryId = jsonEvent.category_id;
        event.categoryString = jsonEvent.category;

        return event;
    }

    public static Place extractPlaceFromEvent(Event event) {
        Place place = new Place();

        place.id = event.id;
        place.name = event.locationString.trim().replace(", null", "").replace("\"", "");
        place.location = event.location;
        place.pictureUri = event.pictureUri;

        return place;
    }

    public static int getCategoryNameIdByCategoryId(@Event.Category int category) {

        int resourceId = Event.OTHER;

        switch (category) {
            case Event.FUN:
                resourceId = R.string.category_fun;
                break;
            case Event.CINEMA:
                resourceId = R.string.category_cinema;
                break;
            case Event.CULTURE:
                resourceId = R.string.category_culture;
                break;
            case Event.FESTIVAL:
                resourceId = R.string.category_festival;
                break;
            case Event.PROMOTION:
                resourceId = R.string.category_promotion;
                break;
            case Event.SPORT:
                resourceId = R.string.category_sport;
                break;
            case Event.FAIR:
                resourceId = R.string.category_fair;
                break;
            case Event.EDUCATION:
                resourceId = R.string.category_education;
                break;
            case Event.CONCERTS:
                resourceId = R.string.category_concerts;
                break;
            case Event.OTHER:
                resourceId = R.string.category_other;
                break;
        }

        return resourceId;
    }
}