package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.ui.fragments.FragmentEvents;

/**
 * Created by Daniel on 12-Jan-16.
 */
public interface FragmentEventsPresenter {
    void setView(FragmentEvents fragmentEventsView);

    void fetchEvents();

    void filterEvents(String query);

    void fetchEventsFromServer();

    void fetchLastUpdatedTimestamp();
}
