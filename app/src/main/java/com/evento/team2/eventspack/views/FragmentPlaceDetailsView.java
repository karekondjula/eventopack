package com.evento.team2.eventspack.views;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;

import java.util.ArrayList;

/**
 * Created by Daniel on 06-Mar-16.
 */
public interface FragmentPlaceDetailsView {

    void showPlace(Place place);

    void showEventsAtPlace(ArrayList<Event> eventArrayList);
}
