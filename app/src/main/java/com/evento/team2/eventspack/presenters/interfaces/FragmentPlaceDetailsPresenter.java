package com.evento.team2.eventspack.presenters.interfaces;

import android.location.Location;

import com.evento.team2.eventspack.views.FragmentEventDetailsView;
import com.evento.team2.eventspack.views.FragmentPlaceDetailsView;

/**
 * Created by Daniel on 09-Apr-16.
 */
public interface FragmentPlaceDetailsPresenter {

    void setView(FragmentPlaceDetailsView fragmentPlaceDetailsView);

    void fetchPlaceDetails(long placeId);

    void fetchActiveEventsByLocation(long placeId);
}
