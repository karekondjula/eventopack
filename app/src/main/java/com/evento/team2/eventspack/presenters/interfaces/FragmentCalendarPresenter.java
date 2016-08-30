package com.evento.team2.eventspack.presenters.interfaces;

import com.evento.team2.eventspack.views.FragmentCalendarView;

import java.util.ArrayList;

/**
 * Created by Daniel on 13-Feb-16.
 */
public interface FragmentCalendarPresenter {

    void setView(FragmentCalendarView fragmentCalendarView);

    void fetchEvents();

    void fetchEventsOnSelectedDates(ArrayList<Long> selectedDates);
}
