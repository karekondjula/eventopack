package com.evento.team2.eventspack.views;

import com.evento.team2.eventspack.models.Event;

/**
 * Created by Daniel on 06-Mar-16.
 */
public interface FragmentEventDetailsView {

    void showEvent(Event event);

    void notifyUserForSavedEvent(boolean isSaved);

    void setTranslatedDetails(String details);

    void showTranslatingMessage();

    void dismissTranslatingMessage();
}
