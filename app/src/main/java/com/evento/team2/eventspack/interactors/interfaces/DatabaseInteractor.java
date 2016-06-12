package com.evento.team2.eventspack.interactors.interfaces;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;

import java.util.ArrayList;

/**
 * Created by Daniel on 12-Jan-16.
 */
public interface DatabaseInteractor {

    long persistEvent(Event event);

    ArrayList<Event> getActiveEvents(String filter);

    ArrayList<Event> getAllEvents();

    ArrayList<Event> getActiveEventsOnDate(String timestamp);

    ArrayList<Event> getActiveEventsByLocation(String latitude, String longitude, String locationName);

    ArrayList<Event> getSavedEvents(String filter);

    ArrayList<Event> getSavedEventsInNext24Hours(String timestamp);

    ArrayList<Event> getActiveEventsByCategory(@Event.Category int categoryId, String filter);

    Event getEventById(long eventId);

    void setSavedStateOfEvent(Event event, boolean isSaved);

    long persistPlace(Place place);

    void persistPlaces(ArrayList<Place> places);

    ArrayList<Place> getPlaces(String filter);

    Place getPlaceById(long placeId);

    /**
     * Deletes all events older than two months, and all places
     * */
    void cleanUpEventsAndPlaces();

    void changeSaveEvent(Event event, boolean isEventSaved);
}
