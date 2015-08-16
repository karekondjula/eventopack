package com.evento.team2.eventspack.utils;

import com.evento.team2.eventspack.model.Event;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Daniel on 04-Aug-15.
 */
public class Utils {
    public static class Helpers {

        public static final String[] EVENTS = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",};
        public static final String[] EVENTS_DESCRIPTION = {"auch jako", "majkata na princezata", "albert", "donzuav", "japanac",};

        public static ArrayList<Event> events;

        public static ArrayList<Event> createEvents() {

            if (events == null) {
                events = new ArrayList<>();

                int i = 0;

                Event dummyEvent;

                for (String eventNames : EVENTS) {

                    dummyEvent = new Event(eventNames, EVENTS_DESCRIPTION[i++]);
                    dummyEvent.id = new Random().nextLong();
                    dummyEvent.location = new LatLng(new Random().nextInt(90) * (new Random().nextBoolean() ? 1 : -1),
                            new Random().nextInt(180) * (new Random().nextBoolean() ? 1 : -1));

                    dummyEvent.isEventSaved = new Random().nextBoolean();
                    dummyEvent.date = System.currentTimeMillis() + 13579 * new Random().nextInt(97531);
                    dummyEvent.dateString = new SimpleDateFormat("HH:mm dd.MM.yyyy ").format(new Date(dummyEvent.date));

                    events.add(dummyEvent);
                }
            }
            return events;
        }
    }
}
