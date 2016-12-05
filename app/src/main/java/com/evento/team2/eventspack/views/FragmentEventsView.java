package com.evento.team2.eventspack.views;

import com.evento.team2.eventspack.models.Event;

import java.util.ArrayList;

/**
 * Created by Daniel on 10-Jan-16.
 */
public interface FragmentEventsView {

    void showEvents(ArrayList<Event> eventsArrayList);

    void startRefreshAnimation();

    void stopRefreshAnimation();

    void showLastUpdatedTimestampMessage(String lastUpdateTimestamp);

    void showNoInternetConnectionMessage();

    void notifyUserForUpdateInEvent(boolean isSaved, String eventName);
}