package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.views.FragmentSavedEventsView;

/**
 * Created by Daniel on 12-Jan-16.
 */
public interface FragmentSavedEventsPresenter {

    void setView(FragmentSavedEventsView fragmentSavedEventsView);

    void fetchSavedEvents(String query);
}
