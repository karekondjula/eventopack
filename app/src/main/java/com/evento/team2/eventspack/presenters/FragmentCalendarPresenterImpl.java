package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentCalendarPresenter;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentCalendarView;

import java.util.ArrayList;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentCalendarPresenterImpl implements FragmentCalendarPresenter {

    FragmentCalendarView fragmentCalendarView;
    MainThread mainThread;
    DatabaseInteractor databaseInteractor;

    public FragmentCalendarPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(FragmentCalendarView fragmentCalendarView) {
        this.fragmentCalendarView = fragmentCalendarView;
    }

    @Override
    public void fetchEvents() {
        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> eventArrayList = databaseInteractor.getAllEvents();

                mainThread.post(() -> fragmentCalendarView.showCalendarEvents(eventArrayList));
            }
        }.start();
    }

    @Override
    public void fetchEventsOnSelectedDates(ArrayList<Long> selectedDates) {
        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> eventArrayList = databaseInteractor.getAllEvents();

                ArrayList<Event> selectedEventsArrayList = new ArrayList<Event>();

                for (final Event event : eventArrayList) {
                    if (selectedDates.contains(event.startDate)) {
                        selectedEventsArrayList.add(event);
                    }
                }

                mainThread.post(() -> fragmentCalendarView.showEventsForSelectedDates(selectedEventsArrayList));
            }
        }.start();
    }
}
