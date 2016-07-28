package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.views.FragmentEventDetailsView;

/**
 * Created by Daniel on 06-Mar-16.
 */
public interface FragmentEventDetailsPresenter {

    void setView(FragmentEventDetailsView fragmentEventsView);

    void fetchEventDetails(long eventId);

    void changeSavedStateOfEvent(Event event);

}
