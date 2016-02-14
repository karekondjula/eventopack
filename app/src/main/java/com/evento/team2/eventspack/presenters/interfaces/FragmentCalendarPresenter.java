package com.evento.team2.eventspack.presenters.interfaces;

import java.util.ArrayList;

/**
 * Created by Daniel on 13-Feb-16.
 */
public interface FragmentCalendarPresenter {

    void fetchEventsOnSelectedDates(ArrayList<Long> selectedDates);
}
