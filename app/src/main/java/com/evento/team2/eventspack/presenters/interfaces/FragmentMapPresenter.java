package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.views.FragmentMapView;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Daniel on 07-Mar-16.
 */
public interface FragmentMapPresenter {

    void setView(FragmentMapView fragmentMapView);

    void fetchEvents(String lastSelectedDate);

    void fetchPlaces();

    void fetchSavedEvents(long selectedDateTimeStamp);

    Event goToEvent(long eventId);

    void goToPlace(long placeId);

    void markerClicked(Marker marker, int selectedMode);
}
