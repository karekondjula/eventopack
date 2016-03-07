package com.evento.team2.eventspack.views;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Daniel on 07-Mar-16.
 */
public interface FragmentMapView {

    void showFetchingMarkers();

    void showMarkers(ArrayList<MarkerOptions> markerOptionsArrayList);

    void showEventSelected(Event event);

    void showPlaceSelected(Place place);

    void goToLocation(double latitude, double longitude);
}
