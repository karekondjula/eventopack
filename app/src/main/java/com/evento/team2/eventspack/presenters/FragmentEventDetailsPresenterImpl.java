package com.evento.team2.eventspack.presenters;

import com.evento.team2.eventspack.interactors.interfaces.DatabaseInteractor;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.models.Place;
import com.evento.team2.eventspack.presenters.interfaces.FragmentEventDetailsPresenter;
import com.evento.team2.eventspack.provider.EventsDatabase;
import com.evento.team2.eventspack.utils.interfaces.MainThread;
import com.evento.team2.eventspack.views.FragmentEventDetailsView;

import java.util.ArrayList;

/**
 * Created by Daniel on 06-Mar-16.
 */
public class FragmentEventDetailsPresenterImpl implements FragmentEventDetailsPresenter {

    private FragmentEventDetailsView fragmentEventDetailsView;
    private MainThread mainThread;
    private DatabaseInteractor databaseInteractor;

    public FragmentEventDetailsPresenterImpl(MainThread mainThread, DatabaseInteractor databaseInteractor) {
        this.mainThread = mainThread;
        this.databaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(FragmentEventDetailsView fragmentEventDetailsView) {
        this.fragmentEventDetailsView = fragmentEventDetailsView;
    }

    @Override
    public void fetchEventDetails(long eventId) {
        new Thread() {
            @Override
            public void run() {

                final Event event = databaseInteractor.getEventById(eventId);
                mainThread.post(() -> fragmentEventDetailsView.showEvent(event));
            }
        }.start();
    }

    @Override
    public void updateSavedStateOfEvent(Event event, boolean isEventSaved) {
       databaseInteractor.changeSaveEvent(event, event.isEventSaved);
    }
}