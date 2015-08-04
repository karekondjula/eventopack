package com.evento.team2.eventspack.utils;

import com.evento.team2.eventspack.model.Event;

import java.util.ArrayList;

/**
 * Created by Daniel on 04-Aug-15.
 */
public class Utils {
    public static class Helpers {

        public static final String[] EVENTS = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",};
        public static final String[] EVENTS_DESCRIPTION = {"auch jako", "majkata na princezata", "albert", "donzuav", "japanac",};

        public static ArrayList<Event> createEvents() {
            ArrayList<Event> events = new ArrayList<Event>();

            int i = 0;

            for (String eventNames : EVENTS) {
                events.add(new Event(eventNames, EVENTS_DESCRIPTION[i++]));
            }

            return events;
        }
    }
}
