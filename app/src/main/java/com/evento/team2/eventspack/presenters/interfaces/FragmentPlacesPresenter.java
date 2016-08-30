package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.views.FragmentPlacesView;

/**
 * Created by Daniel on 12-Jan-16.
 */
public interface FragmentPlacesPresenter {

    void setView(FragmentPlacesView fragmentEventsView);

    void fetchPlaces(String query);
}
