package com.evento.team2.eventspack.views;

import com.evento.team2.eventspack.models.Event;

import java.util.ArrayList;

/**
 * Created by Daniel on 13-Feb-16.
 */
public interface FragmentCalendarView {

    void showCalendarEvents(ArrayList<Event> eventArrayList);

    void showEventsForSelectedDates(ArrayList<Event> eventArrayList);
}
