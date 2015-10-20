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
    public final static class Helpers {

        public static final String[] EVENTS = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",};
        public static final String[] EVENTS_DESCRIPTION = {"auch jakosadf sdfsdfasdfasdfsadfsad", "majkata na ", "albert", "donzuav", "japanac",};

        public static ArrayList<Event> events;

        public static synchronized ArrayList<Event> createEvents() {

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
                    dummyEvent.startDate = System.currentTimeMillis() + 13579l * new Random().nextInt(97531);
                    dummyEvent.startDateString = new SimpleDateFormat("HH:mm dd.MM.yyyy ").format(new Date(dummyEvent.startDate));

                    events.add(dummyEvent);
                }
            }
            return events;
        }

        public static String getEventsJson() {
            return "[eventArrayList:{\"calendar\":\"JsonEvent Category\",\"id\":\"1\",\"pid\":\"1\",\"tstamp\":\"1438693591\",\"title\":\"Test event 1\",\"alias\":\"test-event-1\",\"author\":\"1\",\"addTime\":\"1\",\"startTime\":\"1438693500\",\"endTime\":\"1438693500\",\"startDate\":\"1438639200\",\"endDate\":null,\"location\":\"neakde\",\"teaser\":null,\"addImage\":\"\",\"singleSRC\":null,\"alt\":\"\",\"size\":\"\",\"imagemargin\":\"\",\"imageUrl\":\"\",\"fullsize\":\"\",\"caption\":\"\",\"floating\":\"above\",\"recurring\":\"\",\"repeatEach\":\"\",\"repeatEnd\":\"0\",\"recurrences\":\"0\",\"addEnclosure\":\"\",\"enclosure\":null,\"source\":\"default\",\"jumpTo\":\"0\",\"articleId\":\"0\",\"url\":\"\",\"target\":\"\",\"cssClass\":\"\",\"noComments\":\"\",\"published\":\"1\",\"start\":\"\",\"stop\":\"\"},{\"calendar\":\"JsonEvent Category\",\"id\":\"2\",\"pid\":\"1\",\"tstamp\":\"1438770775\",\"title\":\"Test event 2\",\"alias\":\"sdfsdf\",\"author\":\"1\",\"addTime\":\"1\",\"startTime\":\"1438770660\",\"endTime\":\"1438770660\",\"startDate\":\"1438725600\",\"endDate\":null,\"location\":\"sdfsdf\",\"teaser\":\"<p>[nbsp]sdf sdfsdf sd fsdfsdfs df<\\/p>\",\"addImage\":\"\",\"singleSRC\":null,\"alt\":\"\",\"size\":\"\",\"imagemargin\":\"\",\"imageUrl\":\"\",\"fullsize\":\"\",\"caption\":\"\",\"floating\":\"above\",\"recurring\":\"\",\"repeatEach\":\"\",\"repeatEnd\":\"0\",\"recurrences\":\"0\",\"addEnclosure\":\"\",\"enclosure\":null,\"source\":\"default\",\"jumpTo\":\"0\",\"articleId\":\"0\",\"url\":\"\",\"target\":\"\",\"cssClass\":\"\",\"noComments\":\"\",\"published\":\"1\",\"start\":\"\",\"stop\":\"\"},{\"calendar\":\"kalendar 2\",\"id\":\"6\",\"pid\":\"2\",\"tstamp\":\"1438857666\",\"title\":\"koncert 1\",\"alias\":\"kon1\",\"author\":\"1\",\"addTime\":\"1\",\"startTime\":\"1438857600\",\"endTime\":\"1439203200\",\"startDate\":\"1438812000\",\"endDate\":\"1439157600\",\"location\":\"vamu\",\"teaser\":\"<p>sdafsd sdf sd fsdfsdf sd<\\/p>\",\"addImage\":\"\",\"singleSRC\":null,\"alt\":\"\",\"size\":\"\",\"imagemargin\":\"\",\"imageUrl\":\"\",\"fullsize\":\"\",\"caption\":\"\",\"floating\":\"above\",\"recurring\":\"\",\"repeatEach\":\"\",\"repeatEnd\":\"0\",\"recurrences\":\"0\",\"addEnclosure\":\"\",\"enclosure\":null,\"source\":\"default\",\"jumpTo\":\"0\",\"articleId\":\"0\",\"url\":\"\",\"target\":\"\",\"cssClass\":\"\",\"noComments\":\"\",\"published\":\"1\",\"start\":\"\",\"stop\":\"\"},{\"calendar\":\"kalendar 2\",\"id\":\"8\",\"pid\":\"2\",\"tstamp\":\"0\",\"title\":\"Add event1 izmenet\",\"alias\":\"ae1\",\"author\":\"1\",\"addTime\":\"1\",\"startTime\":\"1438693500\",\"endTime\":\"1438693500\",\"startDate\":null,\"endDate\":\"0\",\"location\":\"mesto\",\"teaser\":\"<p>test bla bla bla<\\/p>\",\"addImage\":\"\",\"singleSRC\":null,\"alt\":\"\",\"size\":\"\",\"imagemargin\":\"\",\"imageUrl\":\"\",\"fullsize\":\"\",\"caption\":\"\",\"floating\":\"above\",\"recurring\":\"\",\"repeatEach\":\"\",\"repeatEnd\":\"0\",\"recurrences\":\"0\",\"addEnclosure\":\"\",\"enclosure\":null,\"source\":\"default\",\"jumpTo\":\"0\",\"articleId\":\"0\",\"url\":\"\",\"target\":\"\",\"cssClass\":\"\",\"noComments\":\"\",\"published\":\"1\",\"start\":\"\",\"stop\":\"\"}]";
        }
    }
}
