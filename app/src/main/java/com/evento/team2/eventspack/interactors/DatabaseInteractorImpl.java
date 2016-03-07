package com.evento.team2.eventspack.interactors;

import android.location.Geocoder;

import com.evento.team2.eventspack.EventiApplication;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.provider.EventsDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Daniel on 12-Jan-16.
 */
public class DatabaseInteractorImpl implements DatabaseInteractor {

    public DatabaseInteractorImpl(EventiApplication eventiApplication) {
        EventsDatabase.getInstance().openEventsDatabase(eventiApplication);
        EventsDatabase.getInstance().setGeocoder(new Geocoder(eventiApplication, Locale.getDefault()));
    }

    @Override
    public long persistEvent(Event event) {
        return EventsDatabase.getInstance().persistEvent(event);
    }

    @Override
    public ArrayList<Event> getActiveEvents(String filter) {
        return EventsDatabase.getInstance().getEvents(filter, String.valueOf(new Date().getTime()));
    }

    @Override
    public ArrayList<Event> getAllEvents() {
        return EventsDatabase.getInstance().getEvents();
    }

    @Override
    public ArrayList<Event> getActiveEventsOnDate(String timestamp) {
        return EventsDatabase.getInstance().getActiveEventsOnDate(timestamp);
    }

    @Override
    public ArrayList<Event> getActiveEventsByLocation(String latitude, String longitude, String locationName) {
        return EventsDatabase.getInstance().getActiveEventsByLocation(latitude, longitude, locationName, String.valueOf(new Date().getTime()));
    }

    @Override
    public ArrayList<Event> getSavedEvents(String filter) {
        return EventsDatabase.getInstance().getSavedEvents(filter);
    }

    @Override
    public Event getEventById(long eventId) {
        return EventsDatabase.getInstance().getEventById(eventId);
    }

    @Override
    public void setSavedStateOfEvent(Event event, boolean isSaved) {
        EventsDatabase.getInstance().changeSaveEvent(event, isSaved);
    }

    @Override
    public long persistPlace(Place place) {
        return EventsDatabase.getInstance().persistPlace(place);
    }

    @Override
    public void persistPlaces(ArrayList<Place> places) {
        for (Place place : places) {
            persistPlace(place);
        }
    }

    @Override
    public ArrayList<Place> getPlaces(String filter) {
        return EventsDatabase.getInstance().getPlaces(filter);
    }

    @Override
    public Place getPlaceById(long placeId) {
        return EventsDatabase.getInstance().getPlaceById(placeId);
    }

    @Override
    public void cleanUpEventsAndPlaces() {
        EventsDatabase.getInstance().cleanUpEventsAndPlaces();
    }

    @Override
    public void changeSaveEvent(Event event, boolean isEventSaved) {
        EventsDatabase.getInstance().changeSaveEvent(event, event.isEventSaved);
    }
}
