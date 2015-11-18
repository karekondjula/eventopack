package com.evento.team2.eventspack.utils;

import android.text.TextUtils;

import com.evento.team2.eventspack.model.Event;
import com.evento.team2.eventspack.model.Place;
import com.evento.team2.eventspack.soapservice.model.JsonEvent;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 17-Oct-15.
 */
public class ConversionUtils {

    public static Event convertJsonEventToEvent(JsonEvent jsonEvent) {
        Event event = new Event(jsonEvent.title, jsonEvent.teaser);

        event.id = jsonEvent.id;
        event.facebookId = jsonEvent.fb_id;
        event.startTimeStamp = jsonEvent.startTime * 1000; // in ms

        String dateTime = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date(event.startTimeStamp));

        event.startTimeString = dateTime.split(" ")[0];
        event.startDateString = dateTime.split(" ")[1];

        event.endTimeStamp = jsonEvent.endTime * 1000; // in ms
//        event.pictureUri = jsonEvent.imageUrl;
        event.locationString = jsonEvent.location.concat(", " + jsonEvent.city);
        event.location = new LatLng(jsonEvent.lat, jsonEvent.lng);


        return event;
    }

    public static ArrayList<Event> convertJsonEventsArrayListToEventArrayList(ArrayList<JsonEvent> jsonEventArrayList) {

        ArrayList<Event> eventArrayList = new ArrayList();

        for (JsonEvent jsonEvent : jsonEventArrayList) {
            eventArrayList.add(convertJsonEventToEvent(jsonEvent));
        }

        return eventArrayList;
    }

    public static ArrayList<Place> extractPlacesFromEvents(ArrayList<Event> eventArrayList) {
        ArrayList<Place> placeArrayList = new ArrayList<>();

        Place place;
        for (Event event : eventArrayList) {
            if (!TextUtils.isEmpty(event.locationString) && !event.locationString.startsWith("TBA")) {
                place = new Place();

                place.id = event.id;
                place.name = event.locationString.trim();
                place.location = event.location;

                placeArrayList.add(place);
            }
        }

        return placeArrayList;
    }
}
