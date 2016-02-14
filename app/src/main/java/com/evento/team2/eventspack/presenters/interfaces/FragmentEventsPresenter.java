package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.views.FragmentEventsView;

/**
 * Created by Daniel on 12-Jan-16.
 */
public interface FragmentEventsPresenter {

    void setView(FragmentEventsView fragmentEventsView);

    void fetchEvents(String query);

    void fetchEventsFromServer(boolean forceUpdate);

    void fetchLastUpdatedTimestamp();
}
