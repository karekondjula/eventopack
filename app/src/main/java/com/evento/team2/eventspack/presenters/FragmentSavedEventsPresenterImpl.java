package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.NotificationsInteractor;
import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventsPresenter;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventsView;

import java.util.ArrayList;

/**
 * Created by Daniel on 10-Jan-16.
 */
public class FragmentSavedEventsPresenterImpl implements FragmentEventsPresenter {

    FragmentEventsView fragmentEventsView;
    MainThread mainThread;
    DatabaseInteractor databaseInteractor;
    private NotificationsInteractor notificationsInteractor;

    public FragmentSavedEventsPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor,
                                            NotificationsInteractor notificationsInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
        this.notificationsInteractor = notificationsInteractor;
    }

    @Override
    public void setView(FragmentEventsView fragmentEventsView) {
        this.fragmentEventsView = fragmentEventsView;
    }

    @Override
    public void fetchEvents(String query) {

    }

    @Override
    public void fetchEventsFromServer(boolean forceUpdate) {

    }

    @Override
    public void fetchLastUpdatedTimestamp() {

    }

    @Override
    public void fetchSavedEvents(String query) {
        new Thread() {
            @Override
            public void run() {
                ArrayList<Event> savedEventsArrayList = databaseInteractor.getSavedEvents(query);
                mainThread.post(() -> fragmentEventsView.showEvents(savedEventsArrayList));
            }
        }.start();
    }

    @Override
    public void changeSavedStateOfEvent(Event event) {
        event.isEventSaved = !event.isEventSaved;

        databaseInteractor.changeSaveEvent(event, event.isEventSaved);

        if (event.isEventSaved) {
            notificationsInteractor.scheduleNotification(event);
        } else {
            notificationsInteractor.removeScheduleNotification(event);
        }

        fragmentEventsView.notifyUserForUpdateInEvent(event.isEventSaved, event.name);
    }
}